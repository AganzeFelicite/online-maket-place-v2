package com.online_market_place.online_market_place.user.dto

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.user.enums.UserRole
import jakarta.validation.constraints.*

sealed class UserCreateDTO {

    data class Input(

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
        val role: MutableList<UserRole>,
    )

    data class Output(
        val success: Boolean,
        val message: String,
        val data: BaseUserResponse?,
    )
}