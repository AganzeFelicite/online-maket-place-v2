package com.online_market_place.online_market_place.order.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

sealed class OrderItemCreateDTO {
    data class Input(
        @field:NotNull(message = "Product ID cannot be null")
        val productId: Long,

        @field:Min(1, message = "Quantity must be at least 1")
        val quantity: Int
    )


    data class Output(
        val id: Long,
        val productId: Long,
        val productName: String,
        val quantity: Int,
        val price: Double,
        val totalPrice: Double = quantity * price,
        val productImageUrl: String? = null
    )
}