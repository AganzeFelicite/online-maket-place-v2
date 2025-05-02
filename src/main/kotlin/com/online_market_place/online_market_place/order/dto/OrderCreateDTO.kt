package com.online_market_place.online_market_place.order.dto

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.product.enums.ProductOrderStatus
import jakarta.validation.constraints.NotEmpty
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

sealed class OrderCreateDTO {
    data class Input(
        @field:NotNull("User ID is required")
        val userId: Long,

        @field:NotEmpty(message = "Order must have at least one item")
        val items: List<OrderItemCreateDTO.Input> = emptyList()
    )


    data class Output(
        val id: Long,
        val user: BaseUserResponse,
        var status: ProductOrderStatus,
        val createdAt: LocalDateTime,
        val items: List<OrderItemCreateDTO.Output> = emptyList()
    )

}