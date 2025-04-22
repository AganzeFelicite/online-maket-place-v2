package com.online_market_place.online_market_place.mapper

import com.online_market_place.online_market_place.dto.category.CategoryResponse
import com.online_market_place.online_market_place.dto.product.CreateProductRequest
import com.online_market_place.online_market_place.dto.product.ProductResponse
import com.online_market_place.online_market_place.entiy.category.CategoryEntity
import com.online_market_place.online_market_place.entiy.product.ProductEntity

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