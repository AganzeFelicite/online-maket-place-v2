package com.online_market_place.online_market_place.order.entity

import com.online_market_place.online_market_place.product.ProductOrderStatus
import com.online_market_place.online_market_place.user.entity.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")

data class OrderEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    var status: ProductOrderStatus = ProductOrderStatus.PENDING,
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val items: List<OrderItemEntity
            > = mutableListOf(),


    var totalAmount: Double = 0.0
)

