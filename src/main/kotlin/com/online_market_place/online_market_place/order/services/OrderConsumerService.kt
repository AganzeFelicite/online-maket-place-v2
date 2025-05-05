package com.online_market_place.online_market_place.order.services

import com.online_market_place.online_market_place.order.dto.OrderMessage

interface OrderConsumerService {
    fun handleOrderProcessing(message: OrderMessage)

}