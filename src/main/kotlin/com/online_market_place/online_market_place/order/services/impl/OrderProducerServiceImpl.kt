package com.online_market_place.online_market_place.order.services.impl

import com.online_market_place.online_market_place.common.config.rabbitmq.RabbitMQConfig
import com.online_market_place.online_market_place.notification.dto.EmailMessage
import com.online_market_place.online_market_place.order.dto.OrderMessage
import com.online_market_place.online_market_place.order.services.OrderProducer
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class OrderProducerServiceImpl(private val rabbitTemplate: RabbitTemplate) : OrderProducer {

    override fun publishOrderCreatedEvent(orderMessage: OrderMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_ORDERS,
            orderMessage
        )
    }

    override fun publishOrderStatusUpdateEvent(orderMessage: OrderMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_ORDER_STATUS,
            orderMessage
        )
    }

    override fun publishEmailNotificationEvent(message: EmailMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDERS,
            RabbitMQConfig.ROUTING_KEY_EMAIL,
            message
        )
    }


}