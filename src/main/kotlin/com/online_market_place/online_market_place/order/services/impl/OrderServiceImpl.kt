package com.online_market_place.online_market_place.order.services.impl

import com.online_market_place.online_market_place.common.exceptions.ResourceNotFoundException
import com.online_market_place.online_market_place.notification.dto.EmailMessage
import com.online_market_place.online_market_place.notification.services.EmailService
import com.online_market_place.online_market_place.order.dto.*
import com.online_market_place.online_market_place.order.entities.OrderEntity
import com.online_market_place.online_market_place.order.mappers.OrderMapper
import com.online_market_place.online_market_place.order.repositories.OrderRepository
import com.online_market_place.online_market_place.order.services.OrderProducer
import com.online_market_place.online_market_place.order.services.OrderService
import com.online_market_place.online_market_place.product.dto.CreateProductDTO
import com.online_market_place.online_market_place.product.entities.ProductEntity
import com.online_market_place.online_market_place.product.enums.ProductOrderStatus
import com.online_market_place.online_market_place.product.repositories.ProductRepository
import com.online_market_place.online_market_place.product.services.ProductService
import com.online_market_place.online_market_place.user.entities.UserEntity
import com.online_market_place.online_market_place.user.repositories.UserRepository
import com.online_market_place.online_market_place.user.services.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val userService: UserService,
    private val orderProducer: OrderProducer,
    private val productService: ProductService,
    private val emailService: EmailService
) : OrderService {

    @Transactional
    override fun createOrder(order: OrderCreateDTO.Input): OrderCreateDTO.Output {
        val user = userService.getUserById(order.userId)
        val products = productService.getValidatedProducts(order.items.map { it.productId })
        val totalAmount = calculateTotalAmount(order.items, products)

        val orderEntity = OrderMapper().map(order, user, products)
        val savedOrder = orderRepository.save(orderEntity)

        val orderMessage = buildOrderMessage(savedOrder, user, totalAmount)

        orderProducer.publishOrderCreatedEvent(orderMessage)


        return OrderMapper().map(savedOrder)
    }

    private fun getUser(userId: Long): UserEntity {
        return userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found") }
    }

    private fun calculateTotalAmount(
        items: List<OrderItemCreateDTO.Input>,
        products: Map<Long, ProductEntity>
    ): BigDecimal {
        return items.sumOf { item ->
            val product = products[item.productId]!!
            BigDecimal(product.price).multiply(BigDecimal(item.quantity))
        }
    }

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

    override fun getOrderById(orderId: Long): OrderCreateDTO.Output {
        val order = orderRepository.findById(orderId)
            .orElseThrow { ResourceNotFoundException("Order with ID $orderId not found") }

        return OrderMapper().map(order)
    }

    override fun getAllOrders(): List<OrderCreateDTO.Output> {
        val orders = orderRepository.findAll()

        if (orders.isEmpty()) {
            throw ResourceNotFoundException("No orders found")
        }

        return orders.map { OrderMapper().map(it) }
    }

    override fun updateOrderStatus(order: OrderStatusUpdateDTO.Input): OrderStatusUpdateDTO.Output {
        val existingOrder = orderRepository.findById(order.id)
            .orElseThrow { ResourceNotFoundException("Order with ID ${order.id} not found") }

        existingOrder.status = order.status
        return OrderMapper().mapToStatusOut(orderRepository.save(existingOrder))
    }

    override fun getOrdersByUserId(userId: Long): List<OrderCreateDTO.Output> {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User with ID $userId not found") }

        return orderRepository.findAllByUser(user).map { OrderMapper().map(it) }
    }

    override fun getOrdersByProductId(productId: Long): List<OrderCreateDTO.Output> {
        productRepository.findById(productId)
            .orElseThrow { ResourceNotFoundException("Product with ID $productId not found") }

        val orders = orderRepository.findAll().filter { order ->
            order.items.any { it.product.id == productId }
        }

        return orders.map { OrderMapper().map(it) }
    }

    override fun getOrdersByStatus(status: String): List<OrderCreateDTO.Output> {
        val enumStatus = try {
            ProductOrderStatus.valueOf(status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid status: $status")
        }

        return orderRepository.findAllByStatus(enumStatus).map { OrderMapper().map(it) }
    }

    override fun updateOrder(order: OrderUpdateDTO.Input): OrderCreateDTO.Output {
        val existingOrder = orderRepository.findById(order.id)
            .orElseThrow { ResourceNotFoundException("Order with ID ${order.id} not found") }

        val productMap = mutableMapOf<Long, CreateProductDTO.Output>()
        order.items.forEach { item ->
            val product = productService.getProductById(item.itemId)
            productMap[item.itemId] = product
        }

        order.items.forEach { item ->
            val product = productMap[item.itemId]
                ?: throw ResourceNotFoundException("Product with ID ${item.itemId} not found")

            existingOrder.items.find { it.product.id == item.itemId }?.apply {
                quantity = item.quantity
                price = product.price * item.quantity
            } ?: throw ResourceNotFoundException("Item with Product ID ${item.itemId} not found in order")
        }

        return OrderMapper().map(orderRepository.save(existingOrder))
    }

    override fun deleteOrder(orderId: Long): String {
        val order = orderRepository.findById(orderId)
            .orElseThrow { ResourceNotFoundException("Order with ID $orderId not found") }

        orderRepository.delete(order)
        return "Order with ID $orderId has been deleted successfully."
    }

    override fun failOrder(order: OrderCreateDTO.Output, message: OrderMessage, reason: String) {
        val savedOrder = orderRepository.findById(order.id)
            .orElseThrow { ResourceNotFoundException("Order with ID $order.id not found") }
        savedOrder.fail()
        orderRepository.save(savedOrder)
        emailService.publishEmailNotificationEvent(
            EmailMessage(
                to = message.userEmail,
                subject = "Order Failed",
                body = "$reason Order ID: ${message.orderId}"
            )
        )
    }


    override fun confirmedProduct(order: OrderCreateDTO.Output, message: OrderMessage) {
        val savedOrder = orderRepository.findById(order.id)
            .orElseThrow { ResourceNotFoundException("Order with ID $order.id not found") }
        savedOrder.confirm()
        orderRepository.save(savedOrder)
        emailService.publishEmailNotificationEvent(
            EmailMessage(
                to = message.userEmail,
                subject = "Order Confirmed",
                body = "Your order has been confirmed. Order ID: ${message.orderId}"
            )
        )
    }

}