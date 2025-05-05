package com.online_market_place.online_market_place.order.services

import com.online_market_place.online_market_place.order.dto.OrderMessage

interface OrderProducer {
    fun publishOrderCreatedEvent(orderMessage: OrderMessage)
    fun publishOrderStatusUpdateEvent(orderMessage: OrderMessage)

}