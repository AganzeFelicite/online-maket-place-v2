package com.online_market_place.online_market_place.repository.user

import com.online_market_place.online_market_place.entiy.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
    fun findByVerificationToken(token: String): UserEntity?
}