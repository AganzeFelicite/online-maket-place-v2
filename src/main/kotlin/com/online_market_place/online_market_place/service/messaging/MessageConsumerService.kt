package com.online_market_place.online_market_place.service.messaging

import com.online_market_place.online_market_place.config.messaging.RabbitMQConfig
import com.online_market_place.online_market_place.dto.messages.EmailMessage
import com.online_market_place.online_market_place.dto.order.OrderMessage
import com.online_market_place.online_market_place.entiy.enum_.ProductOrderStatus
import com.online_market_place.online_market_place.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.repository.product.ProductOrderRepository
import com.online_market_place.online_market_place.repository.product.ProductRepository
import com.online_market_place.online_market_place.service.service_interface.EmailService
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import kotlin.math.log

@Service
@Slf4j
class MessageConsumerService(
    private val orderRepository: ProductOrderRepository,
    private val productRepository: ProductRepository,
    private val messageProducerService: MessageProducerService
) {

    @Transactional
    @RabbitListener(queues = [RabbitMQConfig.QUEUE_ORDERS])
    fun handleOrderProcessing(message: OrderMessage) {
        try {
            val order = orderRepository.findById(message.orderId)
                .orElseThrow { ResourceNotFoundException("Order not found") }

            for (item in message.items) {
                val product = productRepository.findById(item.productId)
                    .orElseThrow { ResourceNotFoundException("Product ${item.productId} not found") }

                if (product.stock < item.quantity) {
                    order.status = ProductOrderStatus.FAILED
                    orderRepository.save(order)

                    messageProducerService.sendEmailMessage(
                        EmailMessage(
                            to = message.userEmail,
                            subject = "Order Failed",
                            body = "Not enough stock for product ${product.name}. Order ID: ${message.orderId}"
                        )
                    )
                    return
                }

                product.stock -= item.quantity
                productRepository.save(product)
            }

            order.status = ProductOrderStatus.CONFIRMED



            orderRepository.save(order)


            messageProducerService.sendEmailMessage(
                EmailMessage(
                    to = message.userEmail,
                    subject = "Order Confirmed",
                    body = "Your order (ID: ${message.orderId}) has been confirmed."
                )
            )
        } catch (e: Exception) {
            val order = orderRepository.findById(message.orderId)
                .orElseThrow { ResourceNotFoundException("Order not found") }

            order.status = ProductOrderStatus.FAILED
            orderRepository.save(order)

            messageProducerService.sendEmailMessage(
                EmailMessage(
                    to = message.userEmail,
                    subject = "Order Processing Failed",
                    body = "An error occurred while processing your order (ID: ${message.orderId})."
                )
            )
        }
    }
}

