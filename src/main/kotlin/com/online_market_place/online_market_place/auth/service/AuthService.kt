package com.online_market_place.online_market_place.auth.service


import com.online_market_place.online_market_place.auth.dto.AuthResponse
import com.online_market_place.online_market_place.auth.dto.LoginRequest
import com.online_market_place.online_market_place.user.dto.UserRegisterRequest

interface AuthService {
    fun register(request: UserRegisterRequest): String
    fun verifyToken(token: String): String
    fun login(request: LoginRequest): AuthResponse

}