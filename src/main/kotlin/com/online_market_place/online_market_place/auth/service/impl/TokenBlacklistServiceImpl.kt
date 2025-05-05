package com.online_market_place.online_market_place.auth.service.impl

import com.online_market_place.online_market_place.auth.entity.BlacklistedTokenEntity
import com.online_market_place.online_market_place.auth.exception.TokenBlacklistException
import com.online_market_place.online_market_place.auth.repository.BlacklistedTokenRepository
import com.online_market_place.online_market_place.auth.service.TokenBlacklistService
import mu.KotlinLogging
import org.springframework.dao.DataAccessException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TokenBlacklistServiceImpl(private val repository: BlacklistedTokenRepository) : TokenBlacklistService {
    private val logger = KotlinLogging.logger {}

    override fun isBlacklisted(token: String): Boolean {
        return try {
            repository.existsByToken(token)
        } catch (ex: DataAccessException) {
            logger.error("Error checking if token is blacklisted", ex)

            true
        }
    }

    override fun blacklist(token: String, expiryDate: Instant) {
        try {
            val blacklistedToken = BlacklistedTokenEntity(token = token, expiryDate = expiryDate)
            repository.save(blacklistedToken)
            return
        } catch (ex: DataAccessException) {
            logger.error("Failed to blacklist token", ex)
            throw TokenBlacklistException("Failed to blacklist token")
        }
    }

    @Scheduled(fixedRate = 86400000)
    override fun purgeExpiredTokens() {
        try {
            val count = repository.deleteByExpiryDateBefore(Instant.now())
            logger.info("Purged {} expired tokens from blacklist", count)
        } catch (ex: Exception) {
            logger.error("Error purging expired tokens", ex)
            // Scheduled task should not throw exception to prevent disrupting the scheduler
        }
    }
}