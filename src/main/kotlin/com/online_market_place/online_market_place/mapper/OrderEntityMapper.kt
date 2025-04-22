package com.online_market_place.online_market_place.mapper

import com.online_market_place.online_market_place.dto.order.OrderCreateRequest
import com.online_market_place.online_market_place.dto.order.OrderItemRequest
import com.online_market_place.online_market_place.dto.order.OrderResponse
import com.online_market_place.online_market_place.entiy.order.OrderEntity
import com.online_market_place.online_market_place.entiy.order.OrderItemEntity
import com.online_market_place.online_market_place.entiy.product.ProductEntity
import com.online_market_place.online_market_place.entiy.user.UserEntity

fun OrderEntity.toOrderResponse(): OrderResponse {
    return OrderResponse(
        id = id,
        user = user.toUserResponse(),
        status = status,
        createdAt = createdAt,
        items = items.map { it.toOrderItemResponse() }
    )
}


fun OrderCreateRequest.toEntity(
    user: UserEntity,
    productMap: Map<Long, ProductEntity>
): OrderEntity {
    val order = OrderEntity(
        user = user,
        items = items.map {
            val product = productMap[it.productId]
                ?: throw IllegalArgumentException("Product not found with ID: ${it.productId}")
            it.toEntity(product)
        }
    )
    // Assign the parent order to each item (set the order reference)
    order.items.forEach { it.order = order }
    return order
}


fun OrderItemRequest.toEntity(product: ProductEntity): OrderItemEntity {
    return OrderItemEntity(
        product = product,
        quantity = this.quantity,
        price = product.price,
        order = null
    )
}
