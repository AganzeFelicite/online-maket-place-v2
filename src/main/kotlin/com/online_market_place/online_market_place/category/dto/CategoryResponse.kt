package com.online_market_place.online_market_place.category.dto

import com.online_market_place.online_market_place.product.dto.ProductResponse

data class CategoryResponse(
    val id: Long,
    val name: String,
    val products: List<ProductResponse> = emptyList()
)