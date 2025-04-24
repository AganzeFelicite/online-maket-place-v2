package com.online_market_place.online_market_place.order.entity

import com.online_market_place.online_market_place.order.dto.OrderItemResponse
import com.online_market_place.online_market_place.order.dto.OrderResponse
import com.online_market_place.online_market_place.product.entity.ProductEntity
import com.online_market_place.online_market_place.user.mapper.toUserResponse
import jakarta.persistence.*


fun OrderItemEntity.toOrderItemResponse(): OrderItemResponse {
    return OrderItemResponse(
        id = id,
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
        items = this.items.map { it.toOrderItemResponse() },
        createdAt = this.createdAt,
    )
}

@Entity
@Table(name = "order_items")

data class OrderItemEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne

    @JoinColumn(name = "order_id")
    var order: OrderEntity? = null,


    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: ProductEntity,

    var quantity: Int,
    var price: Double
)