package com.online_market_place.online_market_place.auth.controller

import com.online_market_place.online_market_place.auth.dto.AuthResponse
import com.online_market_place.online_market_place.auth.dto.LoginRequest
import com.online_market_place.online_market_place.auth.service.AuthService
import com.online_market_place.online_market_place.auth.service.TokenBlacklistService
import com.online_market_place.online_market_place.common.ApiResponse
import com.online_market_place.online_market_place.common.config.security.JwtUtil
import com.online_market_place.online_market_place.user.dto.UserRegisterRequest
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/auth")
@Suppress("unused") // Suppress unused warning for the class since these are controllers
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtUtil,
    private val tokenBlacklistService: TokenBlacklistService
) {

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    fun register(@Valid @RequestBody request: UserRegisterRequest): ResponseEntity<String> {
        return ResponseEntity.ok(authService.register(request))
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify email address")
    fun verifyEmail(@RequestParam token: String): ResponseEntity<ApiResponse> {
        val message = authService.verifyToken(token)
        val response = ApiResponse(success = true, message = message)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    @Operation(summary = "Login user ")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user/ blacklisting the token")
    fun logout(@RequestHeader("Authorization") authHeader: String?): ResponseEntity<ApiResponse> {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            val expiryDate = jwtService.extractExpiration(token)
            tokenBlacklistService.blacklist(token, expiryDate)
            return ResponseEntity.ok(ApiResponse(success = true, message = "Logged out successfully"))
        }

        return ResponseEntity.badRequest().body(ApiResponse(success = false, message = "No token provided"))
    }
}


