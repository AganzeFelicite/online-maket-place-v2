package com.online_market_place.online_market_place.mapper

import com.online_market_place.online_market_place.dto.review.CreateReviewRequest
import com.online_market_place.online_market_place.dto.review.ReviewResponse
import com.online_market_place.online_market_place.dto.review.ReviewerResponse
import com.online_market_place.online_market_place.entiy.product.ProductEntity
import com.online_market_place.online_market_place.entiy.product.ProductReviewEntity
import com.online_market_place.online_market_place.entiy.user.UserEntity


fun ProductReviewEntity.toReviewResponse(): ReviewResponse {
    return ReviewResponse(
        id = this.id ?: 0L,
        rating = this.rating,
        comment = this.comment,
        createdAt = this.createdAt,
        reviewer = ReviewerResponse(
            id = this.user.id ?: 0L,
            username = this.user.username,
            email = this.user.email
        )
    )
}

fun CreateReviewRequest.toProductReviewEntity(user: UserEntity, product: ProductEntity): ProductReviewEntity {
    return ProductReviewEntity(
        rating = this.rating,
        comment = this.comment,
        user = user,
        product = product // This will be set later in the service layer

    )
}