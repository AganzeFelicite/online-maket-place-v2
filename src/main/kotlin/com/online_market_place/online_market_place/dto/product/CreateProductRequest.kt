package com.online_market_place.online_market_place.dto.product
import jakarta.validation.constraints.*
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


