package com.online_market_place.online_market_place.common.config.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {
    @Value("\${jwt.secret:OFWHFWHWHUHJW}")
    private lateinit var secret: String

    @Value("\${jwt.expiration:3600000}")
    private lateinit var expirationTime: String

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray()) as SecretKey
    }

    fun generateToken(userDetails: UserDetails): String {
        return Jwts.builder()
            .subject(userDetails.username)
            .claim("roles", userDetails.authorities.map { it.authority })
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationTime.toLong()))
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val claims = extractAllClaims(token)
        val username = extractUsername(claims)
        return (username == userDetails.username && !isTokenExpired(claims))
    }

    private fun extractUsername(claims: Claims): String {
        return claims.subject
    }

    private fun isTokenExpired(claims: Claims): Boolean {
        return claims.expiration.before(Date())
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun extractEmail(token: String): String {
        return extractAllClaims(token).subject
    }

    fun extractExpiration(token: String): Instant {
        return extractAllClaims(token).expiration.toInstant()

    }
}
