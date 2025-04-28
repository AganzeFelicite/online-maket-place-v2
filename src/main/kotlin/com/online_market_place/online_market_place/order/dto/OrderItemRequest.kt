package com.online_market_place.online_market_place.order.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class OrderItemRequest(
    @field:NotNull(message = "Product ID cannot be null")
    val productId: Long,

    @field:Min(1, message = "Quantity must be at least 1")
    val quantity: Int
)