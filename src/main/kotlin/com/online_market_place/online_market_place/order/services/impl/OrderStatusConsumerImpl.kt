package com.online_market_place.online_market_place.order.services.impl

import com.online_market_place.online_market_place.common.config.rabbitmq.RabbitMQConfig
import com.online_market_place.online_market_place.order.dto.OrderMessage
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
@Suppress("unused")
class OrderStatusConsumerImpl (
    private val messageProducerServiceImpl: OrderProducerServiceImpl
)
{

    @RabbitListener(queues = [RabbitMQConfig.QUEUE_ORDER_STATUS])
    fun handleOrderStatusUpdate(message: OrderMessage) {


    }
}
