package com.online_market_place.online_market_place.user.repositories

import com.online_market_place.online_market_place.user.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity
    fun existsByEmail(email: String): Boolean
    fun findByVerificationToken(token: String): UserEntity

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users", nativeQuery = true)
    fun deleteAllPhysically()
}