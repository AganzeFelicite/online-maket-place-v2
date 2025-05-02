package com.online_market_place.online_market_place.category.mappers

import com.online_market_place.online_market_place.category.dto.CreateCategoryDTO
import com.online_market_place.online_market_place.category.dto.UpdateCategoryDTO

import com.online_market_place.online_market_place.category.entities.CategoryEntity
import com.online_market_place.online_market_place.product.dto.CreateProductDTO
import com.online_market_place.online_market_place.review.dto.ReviewResponse
import com.online_market_place.online_market_place.review.dto.ReviewerResponse

class CategoryMapper {

    fun map(category: CategoryEntity): CreateCategoryDTO.Output {
        return CreateCategoryDTO.Output(
            id = category.id,
            name = category.name,
            products = category.products.map {
                CreateProductDTO.Output(
                    id = it.id,
                    name = it.name,
                    price = it.price,
                    imageUrl = it.productImageUrl,
                    description = it.description,
                    stockQuantity = it.stock,
                    featured = it.isFeatured,
                    category = CreateCategoryDTO.Output(
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
                                id = review.user.id,
                                username = review.user.username,
                                email = review.user.email
                            )
                        )
                    }
                )
            }
        )
    }

    fun map(categoryRequest: CreateCategoryDTO.Input): CategoryEntity {
        return CategoryEntity(
            name = categoryRequest.name
        )
    }

    fun mapToUpdateResponse(entity: CategoryEntity): UpdateCategoryDTO.Output {
        return UpdateCategoryDTO.Output(
            id = entity.id,
            name = entity.name
        )
    }
}

