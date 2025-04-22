package com.online_market_place.online_market_place.service.service_interface


import com.online_market_place.online_market_place.dto.auth.AuthResponse
import com.online_market_place.online_market_place.dto.user.LoginRequest
import com.online_market_place.online_market_place.dto.user.UserRegisterRequest
import com.online_market_place.online_market_place.repository.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder

interface AuthService{
    fun register(request: UserRegisterRequest): String
    fun verifyEmail(email: String): String
    fun login(request: LoginRequest): AuthResponse

}