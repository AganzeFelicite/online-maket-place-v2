package com.online_market_place.online_market_place.review.dto

import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long?,
    val rating: Int,
    val comment: String,
    val createdAt: LocalDateTime,
    val reviewer: ReviewerResponse
)