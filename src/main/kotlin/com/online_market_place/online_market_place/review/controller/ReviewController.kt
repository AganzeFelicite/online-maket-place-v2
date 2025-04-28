package com.online_market_place.online_market_place.review.controller

import com.online_market_place.online_market_place.common.ApiResponse
import com.online_market_place.online_market_place.review.dto.CreateReviewRequest
import com.online_market_place.online_market_place.review.dto.ReviewResponse
import com.online_market_place.online_market_place.review.dto.UpdateReviewRequest
import com.online_market_place.online_market_place.review.service.ReviewService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/reviews")
@SecurityRequirement(name = "bearerAuth")
@Suppress("unused")
class ReviewController(
    private val reviewService: ReviewService
) {

    @GetMapping
    @Operation(summary = "Get all reviews")
    fun getAllReviews(): ResponseEntity<List<ReviewResponse>> {
        return ResponseEntity.ok(reviewService.getAllReviews())
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID")
    fun getReviewById(@PathVariable id: Long): ResponseEntity<ReviewResponse> {
        return ResponseEntity.ok(reviewService.getReviewById(id))
    }

    @PostMapping
    @Operation(summary = "Create a new review")
    fun createReview(@Valid @RequestBody reviewRequest: CreateReviewRequest): ResponseEntity<ReviewResponse> {
        val createdReview = reviewService.createReview(reviewRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview)
    }

    @PutMapping
    @Operation(summary = "Update review")
    fun updateReview(@Valid @RequestBody updateRequest: UpdateReviewRequest): ResponseEntity<ReviewResponse> {
        return ResponseEntity.ok(reviewService.updateReview(updateRequest))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review", security = [SecurityRequirement(name = "bearerAuth")])
    fun deleteReview(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        val apiResponse = ApiResponse(
            message = "Review with ID $id deleted successfully",
            success = true
        )

        return ResponseEntity.ok(apiResponse)
    }
}