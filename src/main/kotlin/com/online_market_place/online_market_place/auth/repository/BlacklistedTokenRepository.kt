package com.online_market_place.online_market_place.auth.repository

import com.online_market_place.online_market_place.auth.entity.BlacklistedTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
interface BlacklistedTokenRepository : JpaRepository<BlacklistedTokenEntity, Long> {
    fun existsByToken(token: String): Boolean
    fun deleteByExpiryDateBefore(date: Instant)

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM blacklisted_tokens", nativeQuery = true)
    fun deleteAllPhysically()
}