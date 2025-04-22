package com.online_market_place.online_market_place.service.service_interface

import com.online_market_place.online_market_place.dto.review.CreateReviewRequest
import com.online_market_place.online_market_place.dto.review.ReviewResponse
import com.online_market_place.online_market_place.dto.review.UpdateReviewRequest

interface ReviewService {
    fun getAllReviews(): List<ReviewResponse>

    fun getReviewById(reviewId: Long): ReviewResponse

    fun createReview(review: CreateReviewRequest): ReviewResponse

    fun updateReview(updateReview: UpdateReviewRequest): ReviewResponse

    fun deleteReview(reviewId: Long): String
}