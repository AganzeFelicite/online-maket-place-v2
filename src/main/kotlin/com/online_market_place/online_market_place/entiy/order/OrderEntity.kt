package com.online_market_place.online_market_place.entiy.order

import com.online_market_place.online_market_place.entiy.user.UserEntity
import com.online_market_place.online_market_place.entiy.enum_.ProductOrderStatus
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

