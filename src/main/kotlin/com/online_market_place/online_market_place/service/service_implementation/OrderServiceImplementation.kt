package com.online_market_place.online_market_place.service.service_implementation

import com.online_market_place.online_market_place.dto.order.*
import com.online_market_place.online_market_place.entiy.enum_.ProductOrderStatus
import com.online_market_place.online_market_place.entiy.order.OrderEntity
import com.online_market_place.online_market_place.entiy.product.ProductEntity
import com.online_market_place.online_market_place.entiy.user.UserEntity
import com.online_market_place.online_market_place.exception.InsufficientInventoryException
import com.online_market_place.online_market_place.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.mapper.toEntity
import com.online_market_place.online_market_place.mapper.toOrderResponse
import com.online_market_place.online_market_place.repository.product.ProductOrderRepository
import com.online_market_place.online_market_place.repository.product.ProductRepository
import com.online_market_place.online_market_place.repository.user.UserRepository
import com.online_market_place.online_market_place.service.messaging.MessageProducerService
import com.online_market_place.online_market_place.service.service_interface.OrderService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class OrderServiceImplementation(
    private val orderRepository: ProductOrderRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val messageProducerService: MessageProducerService
)
    : OrderService
{

    @Transactional
    override fun createOrder(order: OrderCreateRequest): OrderResponse {
        val user = getUser(order.userId)
        val products = getValidatedProducts(order.items.map { it.productId })
        val totalAmount = calculateTotalAmount(order.items, products)

        val orderEntity = buildOrderEntity(order, user, products, totalAmount)
        val savedOrder = orderRepository.save(orderEntity)

        val orderMessage = buildOrderMessage(savedOrder, user, totalAmount)
        messageProducerService.sendOrderCreatedMessage(orderMessage)

        return savedOrder.toOrderResponse()
    }

    /**
     * Method to get a userById
     * @param userId Long
     * @return UserEntity
     */
    private fun getUser(userId: Long): UserEntity {
        return userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found") }
    }


    /**
     * Method to get validated products
     * @param productIds List<Long>
     * @return Map<Long, ProductEntity>
     */
    private fun getValidatedProducts(productIds: List<Long>): Map<Long, ProductEntity> {
        val products = productRepository.findAllById(productIds).associateBy { it.id }
        val missingIds = productIds.filterNot { products.containsKey(it) }

        if (missingIds.isNotEmpty()) {
            throw ResourceNotFoundException("Products not found: $missingIds")
        }

        return products
    }

    /**
     * Method to calculate the total amount of the order
     * @param items List<OrderItemRequest>
     * @param products Map<Long, ProductEntity>
     * @return BigDecimal
     */
    private fun calculateTotalAmount(
        items: List<OrderItemRequest>,
        products: Map<Long, ProductEntity>
    ): BigDecimal {
        return items.sumOf { item ->
            val product = products[item.productId]!!
            BigDecimal(product.price).multiply(BigDecimal(item.quantity))
        }
    }

    /**
     * Method to build the order entity
     * @param request OrderCreateRequest
     * @param user UserEntity
     * @param products Map<Long, ProductEntity>
     * @return OrderEntity
     *
     */

    private fun buildOrderEntity(
        request: OrderCreateRequest,
        user: UserEntity,
        products: Map<Long, ProductEntity>,
        totalAmount: BigDecimal
    ): OrderEntity {
        return request.toEntity(user, products).apply {
            status = ProductOrderStatus.PENDING
            this.totalAmount = totalAmount.toDouble()
            createdAt = LocalDateTime.now()
        }
    }

    /**
     * Method to build the order message
     * @param order OrderEntity
     * @param user UserEntity
     * @param totalAmount BigDecimal
     * @return OrderMessage
     */
    private fun buildOrderMessage(
        order: OrderEntity,
        user: UserEntity,
        totalAmount: BigDecimal
    ): OrderMessage {
        return OrderMessage(
            orderId = order.id,
            userId = user.id,
            userEmail = user.email,
            items = order.items.map { item ->
                OrderItemMessage(
                    productId = item.product.id,
                    productName = item.product.name,
                    quantity = item.quantity,
                    price = item.product.price
                )
            },
            totalAmount = totalAmount,
            status = ProductOrderStatus.PENDING,
            createdAt = order.createdAt
        )
    }



    override fun getOrderById(orderId: Long): OrderResponse {
        val order = orderRepository.findById(orderId)
            .orElseThrow { ResourceNotFoundException("Order with ID $orderId not found") }

        return order.toOrderResponse()
    }

    override fun getAllOrders(): List<OrderResponse> {
        val orders = orderRepository.findAll()

        if (orders.isEmpty()) {
            throw ResourceNotFoundException("No orders found")
        }

        return orders.map { it.toOrderResponse() }
    }

    override fun updateOrderStatus(order: OrderStatusUpdate): OrderResponse {
        val existingOrder = orderRepository.findById(order.id)
            .orElseThrow { ResourceNotFoundException("Order with ID ${order.id} not found") }

        existingOrder.status = order.status
        return orderRepository.save(existingOrder).toOrderResponse()
    }


    override fun getOrdersByUserId(userId: Long): List<OrderResponse> {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User with ID $userId not found") }

        return orderRepository.findAllByUser(user).map { it.toOrderResponse() }
    }


    override fun getOrdersByProductId(productId: Long): List<OrderResponse> {
        val product = productRepository.findById(productId)
            .orElseThrow { ResourceNotFoundException("Product with ID $productId not found") }

        val orders = orderRepository.findAll().filter { order ->
            order.items.any { it.product.id == productId }
        }

        return orders.map { it.toOrderResponse() }
    }

    override fun getOrdersByStatus(status: String): List<OrderResponse> {
        val enumStatus = try {
            ProductOrderStatus.valueOf(status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid status: $status")
        }

        return orderRepository.findAllByStatus(enumStatus).map { it.toOrderResponse() }
    }


    override fun updateOrder(order: OrderUpdateRequest): OrderResponse {
        val existingOrder = orderRepository.findById(order.id)
            .orElseThrow { ResourceNotFoundException("Order with ID ${order.id} not found") }

        val productMap = productRepository.findAllById(order.items.map { it.itemId }).associateBy { it.id }

        val updatedItems = order.items.map { item ->
            val product = productMap[item.itemId]
                ?: throw ResourceNotFoundException("Product with ID ${item.itemId} not found")

            existingOrder.items.find { it.product.id == item.itemId }?.apply {
                quantity = item.quantity
                price = product.price * item.quantity
            } ?: throw ResourceNotFoundException("Item with Product ID ${item.itemId} not found in order")
        }

        return orderRepository.save(existingOrder).toOrderResponse()
    }

    override fun deleteOrder(orderId: Long): String {
        val order = orderRepository.findById(orderId)
            .orElseThrow { ResourceNotFoundException("Order with ID $orderId not found") }

        orderRepository.delete(order)
        return "Order with ID $orderId has been deleted successfully."
    }

}