package com.online_market_place.online_market_place.review.service.impl

import com.online_market_place.online_market_place.common.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.product.repository.ProductRepository
import com.online_market_place.online_market_place.review.dto.CreateReviewRequest
import com.online_market_place.online_market_place.review.dto.ReviewResponse
import com.online_market_place.online_market_place.review.dto.UpdateReviewRequest
import com.online_market_place.online_market_place.review.mapper.toProductReviewEntity
import com.online_market_place.online_market_place.review.mapper.toReviewResponse
import com.online_market_place.online_market_place.review.repository.ReviewRepository
import com.online_market_place.online_market_place.review.service.ReviewService
import com.online_market_place.online_market_place.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
@Suppress("unused")
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) : ReviewService {
    override fun getAllReviews(): List<ReviewResponse> {
        val reviews = reviewRepository.findAll()

        if (reviews.isEmpty()) {
            throw RuntimeException("No reviews found")
        }

        return reviews.map { it.toReviewResponse() }
    }

    override fun getReviewById(reviewId: Long): ReviewResponse {
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { RuntimeException("Review with ID $reviewId not found") }

        return review.toReviewResponse()
    }

    override fun createReview(review: CreateReviewRequest): ReviewResponse {
        val user = userRepository.findById(review.userId)
            .orElseThrow { ResourceNotFoundException("User with ID ${review.userId} not found") }

        val product = productRepository.findById(review.productId)
            .orElseThrow { ResourceNotFoundException("Product with ID ${review.productId} not found") }

        val reviewEntity = review.toProductReviewEntity(user, product)

        return try {
            val savedReview = reviewRepository.save(reviewEntity)
            savedReview.toReviewResponse()
        } catch (e: Exception) {
            throw RuntimeException("Failed to create review: ${e.message}")
        }

    }

    override fun updateReview(updateReview: UpdateReviewRequest): ReviewResponse {
        val existingReview = reviewRepository.findById(updateReview.id)
            .orElseThrow { ResourceNotFoundException("Review with ID ${updateReview.id} not found") }


        existingReview.rating = updateReview.rating
        existingReview.comment = updateReview.comment

        return try {
            val updatedReview = reviewRepository.save(existingReview)
            updatedReview.toReviewResponse()
        } catch (e: Exception) {
            throw RuntimeException("Failed to update review: ${e.message}")
        }
    }

    override fun deleteReview(reviewId: Long): String {
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { ResourceNotFoundException("Review with ID $reviewId not found") }

        return try {
            reviewRepository.delete(review)
            "Review with ID $reviewId deleted successfully"
        } catch (e: Exception) {
            throw RuntimeException("Failed to delete review: ${e.message}")
        }
    }
}