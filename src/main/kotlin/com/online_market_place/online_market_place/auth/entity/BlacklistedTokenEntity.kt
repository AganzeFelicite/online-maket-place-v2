package com.online_market_place.online_market_place.auth.entity

import com.online_market_place.online_market_place.common.base.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.Instant

@Entity
@Table(name = "blacklisted_tokens")
@SQLDelete(sql = "UPDATE blacklisted_tokens SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at is null")
data class BlacklistedTokenEntity(

    @Column(unique = true, nullable = false, length = 500)
    var token: String,

    @Column(nullable = false)
    val expiryDate: Instant

) : BaseEntity() {
    fun isExpired(): Boolean {
        return Instant.now().isAfter(expiryDate)
    }


}