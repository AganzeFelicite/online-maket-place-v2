package com.online_market_place.online_market_place.dto.order

import BaseUserResponse
import UserResponse
import com.online_market_place.online_market_place.entiy.enum_.ProductOrderStatus
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val user: BaseUserResponse,
    val status: ProductOrderStatus,
    val createdAt: LocalDateTime,
    val items: List<OrderItemResponse> = emptyList()
)
