package com.online_market_place.online_market_place.product.mappers

import com.online_market_place.online_market_place.category.entities.CategoryEntity
import com.online_market_place.online_market_place.product.dto.CreateProductDTO
import com.online_market_place.online_market_place.product.entities.ProductEntity
import com.online_market_place.online_market_place.review.mappers.toReviewResponse

fun ProductEntity.toProductResponse(): CreateProductDTO.Output {
    return CreateProductDTO.Output(
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

fun CreateProductDTO.Input.toProductEntity(category: CategoryEntity): ProductEntity {
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