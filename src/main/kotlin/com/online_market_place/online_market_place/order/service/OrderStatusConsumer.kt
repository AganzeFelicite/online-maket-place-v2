package com.online_market_place.online_market_place.order.service

import com.online_market_place.online_market_place.common.config.messaging.RabbitMQConfig
import com.online_market_place.online_market_place.order.dto.OrderMessage
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class OrderStatusConsumer (
    private val messageProducerService: MessageProducerService
)
{
    @RabbitListener(queues = [RabbitMQConfig.QUEUE_ORDER_STATUS])
    fun handleOrderStatusUpdate(message: OrderMessage) {
        // TODO Feedback: use the KotlinLogger to log instead of println
        println("Order status update received:")
        println("Order ID: ${message.orderId}")
        println("Status: ${message.status}")
        println("User: ${message.userEmail}")
        println("Order ID ${message.orderId} status changed to ${message.status}")
    }
}
