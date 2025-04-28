package com.online_market_place.online_market_place.auth.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "blacklisted_tokens")
data class BlacklistedTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false, length = 500)
    val token: String,

    @Column(nullable = false)
    val expiryDate: Instant
)