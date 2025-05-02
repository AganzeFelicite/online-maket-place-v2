package com.online_market_place.online_market_place.user.dto

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.user.enums.UserRole
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

sealed class UserUpdateDTO {
    data class Input(
        @field:Min(value = 1, message = "ID must be a positive number")
        val id: Long,

        @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @field:Pattern(
            regexp = "^[a-zA-Z0-9_-]+$",
            message = "Username can only contain letters, numbers, underscores and hyphens"
        )
        val username: String? = null,

        @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        @field:Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must contain at least one letter and one number"
        )
        val password: String? = null,

        val enabled: Boolean? = null,

        @field:NotEmpty(message = "At least one role must be specified when updating roles")
        val role: Set<UserRole>? = null,


        )

    data class Output(
        val success: Boolean,
        val message: String? = null,
        val data: BaseUserResponse,
    )
}