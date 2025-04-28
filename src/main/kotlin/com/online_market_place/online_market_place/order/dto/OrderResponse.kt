package com.online_market_place.online_market_place.order.dto

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.product.enum_.ProductOrderStatus
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val user: BaseUserResponse,
    val status: ProductOrderStatus,
    val createdAt: LocalDateTime,
    val items: List<OrderItemResponse> = emptyList()
)
