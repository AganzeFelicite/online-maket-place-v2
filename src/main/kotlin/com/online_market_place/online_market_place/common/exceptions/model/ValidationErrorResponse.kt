package com.online_market_place.online_market_place.common.exceptions.model

/**
 * Extended error response for validation errors with field details
 */
data class ValidationErrorResponse(
    val errorResponse: ErrorResponse,
    val fieldErrors: List<FieldValidationError>
)
