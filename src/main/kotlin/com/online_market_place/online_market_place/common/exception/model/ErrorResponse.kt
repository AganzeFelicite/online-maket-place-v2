package com.online_market_place.online_market_place.common.exception.model

import java.time.LocalDateTime

/**
 * Response DTO for error messages
 */
data class ErrorResponse(
    val status: Int,
    val message: String,
    val errorCode: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val path: String
)