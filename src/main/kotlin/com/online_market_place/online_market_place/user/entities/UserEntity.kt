package com.online_market_place.online_market_place.user.entities

import com.online_market_place.online_market_place.common.base.BaseEntity
import com.online_market_place.online_market_place.order.entities.OrderEntity
import com.online_market_place.online_market_place.review.entities.ReviewEntity
import com.online_market_place.online_market_place.user.enums.UserRole
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE id=?")
@SQLRestriction("deleted_at is null")
data class UserEntity(
    @Column(unique = true)
    var email: String,

    var username: String,

    var password: String,

    var enabled: Boolean = false,

    @Enumerated(EnumType.STRING)
    var role: Set<UserRole> = setOf(UserRole.CUSTOMER),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val orders: List<OrderEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val reviews: List<ReviewEntity>? = mutableListOf(),

    var verificationToken: String?,

    var tokenExpiryDate: LocalDateTime?,

    ) : BaseEntity() {
    fun updateUserDetails(
        email: String? = null,
        username: String? = null,
        password: String? = null,
        enabled: Boolean? = null,
        role: Set<UserRole>? = null,
        verificationToken: String? = null,
        tokenExpiryDate: LocalDateTime? = null
    ) {
        email?.let { this.email = it }
        username?.let { this.username = it }
        password?.let { this.password = it }
        enabled?.let { this.enabled = it }
        role?.let { this.role = it }
        verificationToken?.let { this.verificationToken = it }
        tokenExpiryDate?.let { this.tokenExpiryDate = it }
    }

    fun isTokenExpired(): Boolean {
        return tokenExpiryDate?.isBefore(LocalDateTime.now()) ?: true
    }

    fun verifyEmail(token: String): Boolean {
        return if (this.verificationToken == token) {
            this.enabled = true
            this.verificationToken = null
            this.tokenExpiryDate = null
            true
        } else {
            false
        }
    }

}