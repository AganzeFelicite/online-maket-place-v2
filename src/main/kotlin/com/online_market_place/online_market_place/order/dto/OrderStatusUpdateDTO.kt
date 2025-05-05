package com.online_market_place.online_market_place.order.dto

import com.online_market_place.online_market_place.product.enums.ProductOrderStatus
import org.jetbrains.annotations.NotNull

sealed class OrderStatusUpdateDTO {
    data class Input(
        @field:NotNull("Order ID cannot be null")
        val id: Long,

        @field:NotNull("Status cannot be null")
        val status: ProductOrderStatus = ProductOrderStatus.PENDING
    )

    data class Output(
        val id: Long,
        val status: ProductOrderStatus
    )
}