package com.online_market_place.online_market_place.order.service

import com.online_market_place.online_market_place.auth.message.EmailMessage
import com.online_market_place.online_market_place.order.dto.OrderMessage

interface MessageConsumerService {
    fun handleOrderProcessing(message: OrderMessage)
    fun handleOrderEmailConfirmation(message: EmailMessage)
}