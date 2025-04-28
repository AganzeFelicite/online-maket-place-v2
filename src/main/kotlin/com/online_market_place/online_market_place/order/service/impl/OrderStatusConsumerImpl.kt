package com.online_market_place.online_market_place.order.service.impl

import com.online_market_place.online_market_place.common.config.messaging.RabbitMQConfig
import com.online_market_place.online_market_place.order.dto.OrderMessage
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
@Suppress("unused")
class OrderStatusConsumerImpl (
    private val messageProducerServiceImpl: MessageProducerServiceImpl
)
{

    @RabbitListener(queues = [RabbitMQConfig.QUEUE_ORDER_STATUS])
    fun handleOrderStatusUpdate(message: OrderMessage) {


    }
}
