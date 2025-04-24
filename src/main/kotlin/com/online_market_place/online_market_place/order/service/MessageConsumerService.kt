package com.online_market_place.online_market_place.order.service

import com.online_market_place.online_market_place.auth.message.EmailMessage
import com.online_market_place.online_market_place.common.config.messaging.RabbitMQConfig
import com.online_market_place.online_market_place.common.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.notification.EmailNotSentException
import com.online_market_place.online_market_place.order.dto.OrderMessage
import com.online_market_place.online_market_place.order.repository.OrderRepository
import com.online_market_place.online_market_place.product.ProductOrderStatus
import com.online_market_place.online_market_place.product.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

// TODO Feedback: Create an interface for this
@Service
class MessageConsumerService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val messageProducerService: MessageProducerService,
    private val mailSender: JavaMailSender,
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

    @RabbitListener(queues = [RabbitMQConfig.QUEUE_EMAIL])
    fun handleOrderEmailConfirmation(message: EmailMessage) {
        try {


            val mailMessage = SimpleMailMessage()
            mailMessage.setTo(message.to)
            mailMessage.subject = message.subject
            mailMessage.text = message.body

            mailSender.send(mailMessage)


        } catch (e: Exception) {
               throw EmailNotSentException("Failed to send email: ${e.message}")
        }
    }
}

