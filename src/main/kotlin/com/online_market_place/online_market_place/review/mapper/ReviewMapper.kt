package com.online_market_place.online_market_place.review.mapper

import com.online_market_place.online_market_place.product.entity.ProductEntity
import com.online_market_place.online_market_place.review.dto.CreateReviewRequest
import com.online_market_place.online_market_place.review.dto.ReviewResponse
import com.online_market_place.online_market_place.review.dto.ReviewerResponse
import com.online_market_place.online_market_place.review.entity.ReviewEntity
import com.online_market_place.online_market_place.user.entity.UserEntity


fun ReviewEntity.toReviewResponse(): ReviewResponse {
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

fun CreateReviewRequest.toProductReviewEntity(user: UserEntity, product: ProductEntity): ReviewEntity {
    return ReviewEntity(
        rating = this.rating,
        comment = this.comment,
        user = user,
        product = product // This will be set later in the service layer

    )
}