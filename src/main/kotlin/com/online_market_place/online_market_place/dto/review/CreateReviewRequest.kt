package com.online_market_place.online_market_place.dto.review

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class CreateReviewRequest(
    @field:NotNull(message = "Rating cannot be null")
    @field:Min(value = 1, message = "Rating must be at least 1")
    @field:Max(value = 5, message = "Rating must not exceed 5")
    val rating: Int,

    @field:NotBlank(message = "Comment cannot be blank")
    @field:Size(min = 3, max = 500, message = "Comment must be between 3 and 500 characters")
    val comment: String,

    @field:NotNull(message = "User ID cannot be null")
    @field:Positive(message = "User ID must be positive")
    val userId: Long,

    @field:NotNull(message = "Product ID cannot be null")
    @field:Positive(message = "Product ID must be positive")
    val productId: Long
)