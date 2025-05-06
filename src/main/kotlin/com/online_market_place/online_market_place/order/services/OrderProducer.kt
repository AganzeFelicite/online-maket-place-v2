package com.online_market_place.online_market_place.order.services

import com.online_market_place.online_market_place.notification.dto.EmailMessage
import com.online_market_place.online_market_place.order.dto.OrderMessage

interface OrderProducer {
    fun publishOrderCreatedEvent(orderMessage: OrderMessage)
    fun publishOrderStatusUpdateEvent(orderMessage: OrderMessage)

    fun publishEmailNotificationEvent(message: EmailMessage)

}