package com.online_market_place.online_market_place.category.dto

import com.online_market_place.online_market_place.product.dto.CreateProductDTO
import jakarta.validation.constraints.NotNull


sealed class CreateCategoryDTO {
    data class Input(
        @field:NotNull(message = "Category name cannot be null")
        val name: String
    )

    data class Output(
        val id: Long,
        val name: String,
        val products: List<CreateProductDTO.Output> = emptyList()
    )
}

