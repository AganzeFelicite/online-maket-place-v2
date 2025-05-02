package com.online_market_place.online_market_place.common.exceptions.model

/**
 * Data class that represents a validation error for a specific field
 */
data class FieldValidationError(
    val field: String,
    val message: String,
    val rejectedValue: Any?
)
