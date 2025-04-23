package com.online_market_place.online_market_place.entiy.product

import com.online_market_place.online_market_place.entiy.user.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "reviews")
data class ProductReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var rating: Int,
    var comment: String,
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: ProductEntity
)
