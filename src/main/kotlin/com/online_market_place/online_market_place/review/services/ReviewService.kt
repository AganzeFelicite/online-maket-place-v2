package com.online_market_place.online_market_place.review.services

import com.online_market_place.online_market_place.review.dto.CreateReviewRequest
import com.online_market_place.online_market_place.review.dto.ReviewResponse
import com.online_market_place.online_market_place.review.dto.UpdateReviewRequest

interface ReviewService {
    fun getAllReviews(): List<ReviewResponse>

    fun getReviewById(reviewId: Long): ReviewResponse

    fun createReview(review: CreateReviewRequest): ReviewResponse

    fun updateReview(updateReview: UpdateReviewRequest): ReviewResponse

    fun deleteReview(reviewId: Long): String
}