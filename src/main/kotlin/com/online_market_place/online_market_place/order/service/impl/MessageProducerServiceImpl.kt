package com.online_market_place.online_market_place.order.service.impl

import com.online_market_place.online_market_place.auth.message.EmailMessage
import com.online_market_place.online_market_place.common.config.messaging.RabbitMQConfig
import com.online_market_place.online_market_place.order.dto.OrderMessage
import com.online_market_place.online_market_place.order.service.MessageProducer
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
@Suppress("unused")
class MessageProducerServiceImpl(private val rabbitTemplate: RabbitTemplate) : MessageProducer {

    override fun sendOrderCreatedMessage(orderMessage: OrderMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_ORDERS,
            orderMessage
        )
    }

    override fun sendOrderStatusUpdateMessage(orderMessage: OrderMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_ORDER_STATUS,
            orderMessage
        )
    }

    override fun sendEmailMessage(message: EmailMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_EMAIL,
            message

        )
    }

}