package com.online_market_place.online_market_place.dto.category

import jakarta.validation.constraints.NotNull

data class UpdateCategoryRequest(
    @field:NotNull(message = "Category ID cannot be null")
    val id: Long,

    @field:NotNull(message = "Category name cannot be null")
    val name: String
)
