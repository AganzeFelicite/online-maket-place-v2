package com.online_market_place.online_market_place.dto.user

import com.online_market_place.online_market_place.entiy.enum_.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserRegisterRequest(

    @field:Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "Email format is invalid"
    )
    @field:Email(message = "Email format is invalid")
    val email: String,

    @field:NotBlank(message = "Username is required")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
        message = "Password must contain at least one digit, one lowercase, one uppercase letter, and one special character"
    )
    val password: String,

    @field:NotEmpty(message = "At least one role is required")
    val role: Set<UserRole> = setOf(UserRole.USER)
)
