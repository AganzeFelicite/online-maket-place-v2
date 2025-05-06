package com.online_market_place.online_market_place.order.services.impl

import com.online_market_place.online_market_place.common.config.rabbitmq.RabbitMQConfig
import com.online_market_place.online_market_place.order.dto.OrderMessage
import com.online_market_place.online_market_place.order.services.OrderConsumerService
import com.online_market_place.online_market_place.order.services.OrderService
import com.online_market_place.online_market_place.product.services.ProductService
import jakarta.transaction.Transactional
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class OrderConsumerServiceImpl(
    private val orderService: OrderService,
    private val productService: ProductService,
    private val mailSender: JavaMailSender,
) : OrderConsumerService {


    @Transactional
    @RabbitListener(queues = [RabbitMQConfig.QUEUE_ORDERS])
    override fun handleOrderProcessing(message: OrderMessage) {
        try {
            val order = orderService.getOrderById(message.orderId)

            var totalOrderAmount = 0.0

            for (item in message.items) {
                val product = productService.getProductById(item.productId)

                if (product.stockQuantity < item.quantity) {
                    orderService.failOrder(
                        order = order, message = message,
                        reason = "Insufficient stock for product ID: ${item.productId}"
                    )
                    return
                }

                productService.updateProductStock(item.productId, item.quantity)

                val itemTotal = product.price * item.quantity
                totalOrderAmount += itemTotal
            }

            orderService.updateOrderTotal(order = order, totalAmount = totalOrderAmount)

            orderService.confirmedProduct(order = order, message = message)

        } catch (e: Exception) {
            orderService.failOrder(
                order = orderService.getOrderById(message.orderId),
                message = message,
                reason = "An error occurred while processing the order: ${e.message}"
            )
        }
    }

}

