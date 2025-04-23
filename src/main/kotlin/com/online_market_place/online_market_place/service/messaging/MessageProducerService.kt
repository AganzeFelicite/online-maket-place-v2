package com.online_market_place.online_market_place.service.messaging

import com.online_market_place.online_market_place.config.messaging.RabbitMQConfig
import com.online_market_place.online_market_place.dto.messages.EmailMessage
import com.online_market_place.online_market_place.dto.order.OrderMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

// TODO Feedback: Create an interface for this
@Service
class MessageProducerService(private val rabbitTemplate: RabbitTemplate) {

    fun sendOrderCreatedMessage(orderMessage: OrderMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_ORDERS,
            orderMessage
        )
    }

    fun sendOrderStatusUpdateMessage(orderMessage: OrderMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_ORDER_STATUS,
            orderMessage
        )
    }

    fun sendEmailMessage(message: EmailMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_EMAIL,
            message

        )
    }

}