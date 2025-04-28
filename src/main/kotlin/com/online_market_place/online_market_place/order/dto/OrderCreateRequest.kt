package com.online_market_place.online_market_place.order.dto

import jakarta.validation.constraints.NotEmpty
import org.jetbrains.annotations.NotNull

data class OrderCreateRequest(
    @field:NotNull("User ID is required")
    val userId: Long,

    @field:NotEmpty(message = "Order must have at least one item")
    val items: List<OrderItemRequest>
)