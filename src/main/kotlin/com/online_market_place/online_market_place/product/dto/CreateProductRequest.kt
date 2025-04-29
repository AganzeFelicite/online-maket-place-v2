package com.online_market_place.online_market_place.product.dto

import com.online_market_place.online_market_place.category.dto.CategoryResponse
import com.online_market_place.online_market_place.review.dto.ReviewResponse
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

// TODO rename these with the DTO suffix and put input and output classes inside
data class CreateProductRequest(

    @field:NotBlank(message = "Product name is required")
    val name: String,

    @field:NotBlank(message = "Description is required")
    val description: String,

    @field:Positive(message = "Price must be greater than zero")
    val price: Double,

    @field:Min(value = 0, message = "Stock quantity cannot be negative")
    val stockQuantity: Int,

    val featured: Boolean = false,


    val imageUrl: String? = null,

    @field:Positive(message = "Category ID must be a positive number")
    val categoryId: Long
)

sealed class CreateProductDTO {
    data class Input(
        @field:NotBlank(message = "Product name is required")
        val name: String,

        @field:NotBlank(message = "Description is required")
        val description: String,

        @field:Positive(message = "Price must be greater than zero")
        val price: Double,

        @field:Min(value = 0, message = "Stock quantity cannot be negative")
        val stockQuantity: Int,

        val featured: Boolean = false,


        val imageUrl: String? = null,

        @field:Positive(message = "Category ID must be a positive number")
        val categoryId: Long
    )

    data class Output(
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
}


