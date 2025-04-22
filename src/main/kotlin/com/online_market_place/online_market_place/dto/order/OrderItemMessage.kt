package com.online_market_place.online_market_place.dto.order

data class OrderItemMessage(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val price: Double
)
