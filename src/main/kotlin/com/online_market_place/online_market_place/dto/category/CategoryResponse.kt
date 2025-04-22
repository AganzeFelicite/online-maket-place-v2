package com.online_market_place.online_market_place.dto.category

import com.online_market_place.online_market_place.dto.product.ProductResponse

data class CategoryResponse(
    val id: Long,
    val name: String,
    val products: List<ProductResponse> = emptyList()
)