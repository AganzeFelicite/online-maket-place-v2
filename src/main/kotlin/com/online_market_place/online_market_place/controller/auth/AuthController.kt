package com.online_market_place.online_market_place.controller.auth

import com.online_market_place.online_market_place.dto.auth.AuthResponse
import com.online_market_place.online_market_place.dto.user.LoginRequest
import com.online_market_place.online_market_place.dto.user.UserRegisterRequest
import com.online_market_place.online_market_place.service.service_interface.AuthService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    fun register(@Valid @RequestBody request: UserRegisterRequest): ResponseEntity<String> {
        return ResponseEntity.ok(authService.register(request))
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify email address")
    fun verifyEmail(@RequestParam token: String): ResponseEntity<String> {
        // TODO Feedback: Return an object instead of a string
        val message = authService.verifyToken(token)
        return ResponseEntity.ok(message)
    }

    @PostMapping("/login")
    @Operation(summary = "Login user ")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }
}
