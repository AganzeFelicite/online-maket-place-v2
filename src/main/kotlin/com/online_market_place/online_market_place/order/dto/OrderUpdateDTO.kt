package com.online_market_place.online_market_place.order.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

sealed class OrderUpdateDTO {
    data class Input(

        @field:NotNull(message = "Order ID cannot be null")
        val id: Long,

        @field:NotNull(message = "Items list cannot be null")
        @field:NotEmpty(message = "Items list cannot be empty")
        @field:Valid
        val items: List<OrderItemUpdateDTO.Input>,
    )
}