package com.online_market_place.online_market_place.product.dto

import com.online_market_place.online_market_place.category.dto.CategoryResponse
import com.online_market_place.online_market_place.review.dto.ReviewResponse

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val stockQuantity: Int,
    val featured: Boolean,
    val imageUrl: String?,
    val category: CategoryResponse,
    val reviews: List<ReviewResponse> = emptyList()
)
