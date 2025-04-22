package com.online_market_place.online_market_place.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class LoginRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email format is invalid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    val password: String
)