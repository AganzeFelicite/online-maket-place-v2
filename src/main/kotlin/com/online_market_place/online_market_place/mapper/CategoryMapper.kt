package com.online_market_place.online_market_place.mapper

import com.online_market_place.online_market_place.dto.category.CategoryResponse
import com.online_market_place.online_market_place.dto.category.CreateCategoryRequest
import com.online_market_place.online_market_place.dto.product.ProductResponse
import com.online_market_place.online_market_place.dto.review.ReviewResponse
import com.online_market_place.online_market_place.dto.review.ReviewerResponse
import com.online_market_place.online_market_place.entiy.category.CategoryEntity

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