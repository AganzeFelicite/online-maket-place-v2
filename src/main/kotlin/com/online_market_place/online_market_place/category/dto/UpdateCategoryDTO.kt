package com.online_market_place.online_market_place.category.dto

import jakarta.validation.constraints.NotNull


sealed class UpdateCategoryDTO {
    data class Input(
        @field:NotNull(message = "Category ID cannot be null")
        val id: Long,

        @field:NotNull(message = "Category name cannot be null")
        val name: String
    )

    data class Output(
        val id: Long,
        val name: String
    )
}

