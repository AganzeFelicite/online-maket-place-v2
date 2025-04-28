package com.online_market_place.online_market_place.auth.service

import java.time.Instant

interface TokenBlacklistService {
    fun isBlacklisted(token: String): Boolean
    fun blacklist(token: String, expiryDate: Instant)

    fun purgeExpiredTokens()
}