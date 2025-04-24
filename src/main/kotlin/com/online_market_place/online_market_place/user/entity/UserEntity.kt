package com.online_market_place.online_market_place.user.entity

import com.online_market_place.online_market_place.order.entity.OrderEntity
import com.online_market_place.online_market_place.review.entity.ReviewEntity
import com.online_market_place.online_market_place.user.enum_.UserRole
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var email: String,

    var username: String,
    var password: String,
    var enabled: Boolean = false,

    @Enumerated(EnumType.STRING)
    var role: Set<UserRole> = setOf(UserRole.CUSTOMER),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val orders: List<OrderEntity>? = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val reviews: List<ReviewEntity>? = mutableListOf(),

    var createAt: LocalDateTime? = null,

    var update: LocalDateTime? = null,
    var verificationToken: String?,
    var tokenExpiryDate: LocalDateTime?,

    ){
    @PrePersist
    fun prePersist() {
        createAt = LocalDateTime.now()
        update = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        update = LocalDateTime.now()
    }
}