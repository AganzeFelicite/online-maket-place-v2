package com.online_market_place.online_market_place.review.dto

import jakarta.validation.constraints.*

data class UpdateReviewRequest(
    @field:NotNull(message = "Review ID cannot be null")
    @field:Positive(message = "Review ID must be positive")
    val id: Long,

    @field:NotNull(message = "Rating cannot be null")
    @field:Min(value = 1, message = "Rating must be at least 1")
    @field:Max(value = 5, message = "Rating must not exceed 5")
    val rating: Int,

    @field:NotBlank(message = "Comment cannot be blank")
    @field:Size(min = 3, max = 500, message = "Comment must be between 3 and 500 characters")
    val comment: String
)
