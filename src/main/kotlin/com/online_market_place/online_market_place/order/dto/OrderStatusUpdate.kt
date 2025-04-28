package com.online_market_place.online_market_place.order.dto

import com.online_market_place.online_market_place.product.enum_.ProductOrderStatus
import org.jetbrains.annotations.NotNull

class OrderStatusUpdate (
    @field:NotNull("Order ID cannot be null")
    val id: Long,

    @field:NotNull("Status cannot be null")
    val status: ProductOrderStatus = ProductOrderStatus.PENDING
)