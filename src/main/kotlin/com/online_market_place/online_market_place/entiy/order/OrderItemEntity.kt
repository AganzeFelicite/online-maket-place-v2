package com.online_market_place.online_market_place.entiy.order

import com.online_market_place.online_market_place.entiy.product.ProductEntity
import jakarta.persistence.*

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
