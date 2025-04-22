package com.online_market_place.online_market_place.dto.product

import com.online_market_place.online_market_place.dto.category.CategoryResponse
import com.online_market_place.online_market_place.dto.review.ReviewResponse

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
