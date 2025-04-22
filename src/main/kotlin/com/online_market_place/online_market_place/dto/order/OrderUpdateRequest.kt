package com.online_market_place.online_market_place.dto.order

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class OrderUpdateRequest(

    @field:NotNull(message = "Order ID cannot be null")
    val id: Long,

    @field:NotNull(message = "Items list cannot be null")
    @field:NotEmpty(message = "Items list cannot be empty")
    @field:Valid
    val items: List<OrderItemUpdateRequest>
)
