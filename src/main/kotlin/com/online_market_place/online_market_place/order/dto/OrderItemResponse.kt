package com.online_market_place.online_market_place.order.dto
data class OrderItemResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val price: Double,
    val totalPrice: Double = quantity * price,
    val productImageUrl: String? = null
)
