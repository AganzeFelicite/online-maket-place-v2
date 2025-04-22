package com.online_market_place.online_market_place.dto.order

import com.online_market_place.online_market_place.entiy.enum_.ProductOrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderMessage(
    val orderId: Long,
    val userId: Long?,
    val userEmail: String,
    val items: List<OrderItemMessage>,
    val totalAmount: BigDecimal,
    val status: ProductOrderStatus,
    val createdAt: LocalDateTime
)