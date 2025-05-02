package com.online_market_place.online_market_place.auth.service


import com.online_market_place.online_market_place.auth.dto.AuthDTO
import com.online_market_place.online_market_place.user.dto.UserCreateDTO

interface AuthService {
    fun register(request: UserCreateDTO.Input): UserCreateDTO.Output
    fun verifyToken(token: String): String
    fun login(request: AuthDTO.Input): AuthDTO.Out

}