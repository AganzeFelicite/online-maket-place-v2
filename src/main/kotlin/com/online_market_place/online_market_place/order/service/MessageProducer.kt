package com.online_market_place.online_market_place.order.service

import com.online_market_place.online_market_place.auth.message.EmailMessage
import com.online_market_place.online_market_place.order.dto.OrderMessage

interface MessageProducer {
    fun sendOrderCreatedMessage(orderMessage: OrderMessage)
    fun sendOrderStatusUpdateMessage(orderMessage: OrderMessage)
    fun sendEmailMessage(message: EmailMessage)
}