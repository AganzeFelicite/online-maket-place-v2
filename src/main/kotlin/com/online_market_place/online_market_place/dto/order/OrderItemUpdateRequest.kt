package com.online_market_place.online_market_place.dto.order

import org.jetbrains.annotations.NotNull

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class OrderItemUpdateRequest(

    @field:NotBlank(message = "Item ID cannot be null")
    val itemId: Long,

    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int
)
