package com.online_market_place.online_market_place.dto.auth

import BaseUserResponse


data class AuthResponse(
     val token: String,
     val user: BaseUserResponse
)
