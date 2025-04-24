package com.online_market_place.online_market_place.auth.dto

import BaseUserResponse


data class AuthResponse(
     val token: String,
     val user: BaseUserResponse
)
