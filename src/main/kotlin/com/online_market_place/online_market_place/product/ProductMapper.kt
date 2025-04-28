package com.online_market_place.online_market_place.product

import com.online_market_place.online_market_place.category.dto.CategoryResponse
import com.online_market_place.online_market_place.category.entity.CategoryEntity
import com.online_market_place.online_market_place.product.dto.CreateProductRequest
import com.online_market_place.online_market_place.product.dto.ProductResponse
import com.online_market_place.online_market_place.product.entity.ProductEntity
import com.online_market_place.online_market_place.review.mapper.toReviewResponse

fun ProductEntity.toProductResponse(): ProductResponse {
    return ProductResponse(
        id = id,
        name = name,
        description = description,
        price = price,
        stockQuantity = stock,
        featured = isFeatured,
        imageUrl = productImageUrl,
        category = CategoryResponse(
            id = category.id,
            name = category.name
        ),
        reviews = reviews.map { review ->
                review.toReviewResponse()
        }
    )
}

fun CreateProductRequest.toProductEntity(category: CategoryEntity): ProductEntity {
    return ProductEntity(
        name = name,
        description = description,
        price = price,
        stock = stockQuantity,
        isFeatured = featured,
        productImageUrl = imageUrl,
        category = category
    )
}