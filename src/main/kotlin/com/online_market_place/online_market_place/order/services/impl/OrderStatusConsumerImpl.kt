package com.online_market_place.online_market_place.order.services.impl

import com.online_market_place.online_market_place.common.config.rabbitmq.RabbitMQConfig
import com.online_market_place.online_market_place.notification.dto.EmailMessage
import com.online_market_place.online_market_place.notification.services.EmailService

import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class OrderStatusConsumerImpl(
    private val messageProducerServiceImpl: OrderProducerServiceImpl,
    private val emailService: EmailService
) {
    var logger = KotlinLogging.logger {}

    @RabbitListener(queues = [RabbitMQConfig.QUEUE_EMAIL])
    fun handleOrderStatusUpdate(message: EmailMessage) {
        emailService.sendEmail(message)

    }
}
