package com.online_market_place.online_market_place.controller.review
import com.online_market_place.online_market_place.dto.review.CreateReviewRequest
import com.online_market_place.online_market_place.dto.review.ReviewResponse
import com.online_market_place.online_market_place.dto.review.UpdateReviewRequest
import com.online_market_place.online_market_place.service.service_interface.ReviewService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {

    @GetMapping
    fun getAllReviews(): ResponseEntity<List<ReviewResponse>> {
        return ResponseEntity.ok(reviewService.getAllReviews())
    }

    @GetMapping("/{id}")
    fun getReviewById(@PathVariable id: Long): ResponseEntity<ReviewResponse> {
        return ResponseEntity.ok(reviewService.getReviewById(id))
    }

    @PostMapping
    fun createReview(@Valid @RequestBody reviewRequest: CreateReviewRequest): ResponseEntity<ReviewResponse> {
        val createdReview = reviewService.createReview(reviewRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview)
    }

    @PutMapping
    fun updateReview(@Valid @RequestBody updateRequest: UpdateReviewRequest): ResponseEntity<ReviewResponse> {
        return ResponseEntity.ok(reviewService.updateReview(updateRequest))
    }

    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable id: Long): ResponseEntity<String> {
        val message = reviewService.deleteReview(id)
        return ResponseEntity.ok(message)
    }
}