package com.online_market_place.online_market_place.mapper

import com.online_market_place.online_market_place.dto.order.OrderItemResponse
import com.online_market_place.online_market_place.dto.order.OrderResponse
import com.online_market_place.online_market_place.entiy.order.OrderEntity
import com.online_market_place.online_market_place.entiy.order.OrderItemEntity


fun OrderItemEntity.toOrderItemResponse(): OrderItemResponse {
    return OrderItemResponse(
        id = id ?: 0L,
        productId = product.id,
        productName = product.name,
        quantity = quantity,
        price = product.price,
        totalPrice = quantity * product.price,
        productImageUrl = product.productImageUrl,


    )
}

fun OrderEntity.toResponse(): OrderResponse {
    return OrderResponse(
        id = this.id,
        user = this.user.toUserResponse(),
        status = this.status,
        items = this.items.map{ it.toOrderItemResponse() },
        createdAt = this.createdAt,
    )
}