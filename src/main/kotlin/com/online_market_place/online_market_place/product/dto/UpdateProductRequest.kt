package com.online_market_place.online_market_place.product.dto

import jakarta.validation.constraints.*


data class UpdateProductRequest(
    @field:Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    val name: String? = null,

    @field:Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    val description: String? = null,

    @field:DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @field:Digits(integer = 10, fraction = 2, message = "Price can have at most 10 digits and 2 decimal places")
    val price: Double? = null,

    @field:Min(value = 0, message = "Stock quantity cannot be negative")
    val stockQuantity: Int? = null,

    val featured: Boolean? = null,

    @field:Pattern(
        regexp = "^(https?://.*|)$",
        message = "Image URL must be a valid URL starting with http:// or https://"
    )
    val imageUrl: String? = null,

    @field:NotNull(message = "Category ID cannot be null")
    @field:Positive(message = "Category ID must be positive")
    val categoryId: Long
)