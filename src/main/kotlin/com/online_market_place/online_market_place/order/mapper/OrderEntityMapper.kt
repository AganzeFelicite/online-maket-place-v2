package com.online_market_place.online_market_place.order.mapper

import com.online_market_place.online_market_place.order.dto.OrderCreateRequest
import com.online_market_place.online_market_place.order.dto.OrderItemRequest
import com.online_market_place.online_market_place.order.dto.OrderResponse
import com.online_market_place.online_market_place.order.entity.OrderEntity
import com.online_market_place.online_market_place.order.entity.OrderItemEntity
import com.online_market_place.online_market_place.order.entity.toOrderItemResponse
import com.online_market_place.online_market_place.product.entity.ProductEntity
import com.online_market_place.online_market_place.user.entity.UserEntity
import com.online_market_place.online_market_place.user.mapper.toUserResponse

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
