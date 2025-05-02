package com.online_market_place.online_market_place.product.mappers

import com.online_market_place.online_market_place.category.dto.CreateCategoryDTO
import com.online_market_place.online_market_place.category.entities.CategoryEntity
import com.online_market_place.online_market_place.product.dto.CreateProductDTO
import com.online_market_place.online_market_place.product.entities.ProductEntity
import com.online_market_place.online_market_place.review.mappers.toReviewResponse

class ProductMapper {


    fun map(product: ProductEntity): CreateProductDTO.Output {
        return CreateProductDTO.Output(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            stockQuantity = product.stock,
            featured = product.isFeatured,
            imageUrl = product.productImageUrl,
            category = CreateCategoryDTO.Output(
                id = product.category.id,
                name = product.category.name
            ),
            reviews = product.reviews.map { it.toReviewResponse() }
        )
    }


    fun map(products: List<ProductEntity>): List<CreateProductDTO.Output> {
        return products.map { map(it) }
    }


    fun map(input: CreateProductDTO.Input, category: CategoryEntity): ProductEntity {
        return ProductEntity(
            name = input.name,
            description = input.description,
            price = input.price,
            stock = input.stockQuantity,
            isFeatured = input.featured,
            productImageUrl = input.imageUrl,
            category = category
        )
    }
}
