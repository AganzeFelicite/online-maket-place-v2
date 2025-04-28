package com.online_market_place.online_market_place.category.mapper

import com.online_market_place.online_market_place.category.dto.CategoryResponse
import com.online_market_place.online_market_place.category.dto.CreateCategoryRequest
import com.online_market_place.online_market_place.category.entity.CategoryEntity
import com.online_market_place.online_market_place.product.dto.ProductResponse
import com.online_market_place.online_market_place.review.dto.ReviewResponse
import com.online_market_place.online_market_place.review.dto.ReviewerResponse

fun CategoryEntity.toCategoryResponse(): CategoryResponse {
    return CategoryResponse(
        id = id,
        name = name,
        products = products.map {
            ProductResponse(
                id = it.id,
                name = it.name,
                price = it.price,
                imageUrl = it.productImageUrl,
                description = it.description,
                stockQuantity = it.stock,
                featured = it.isFeatured,
                category = CategoryResponse(
                    id = it.category.id,
                    name = it.category.name
                ),
                reviews = it.reviews.map { review ->
                    ReviewResponse(
                        id = review.id,
                        rating = review.rating,
                        comment = review.comment,
                        createdAt = review.createdAt,
                        reviewer = ReviewerResponse(
                            id = review.user.id ?: 0L,
                            username = review.user.username,
                            email = review.user.email
                        )
                    )
                }
            )
        }
    )
}

fun CreateCategoryRequest.toEntity(): CategoryEntity {
    return CategoryEntity(
        name = name
    )
}