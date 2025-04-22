package com.online_market_place.online_market_place.service.service_implementation

import com.online_market_place.online_market_place.security.JwtUtil
import com.online_market_place.online_market_place.dto.auth.AuthResponse
import com.online_market_place.online_market_place.dto.user.LoginRequest
import com.online_market_place.online_market_place.dto.user.UserRegisterRequest

import com.online_market_place.online_market_place.entiy.user.UserEntity
import com.online_market_place.online_market_place.exception.EmailConflictException
import com.online_market_place.online_market_place.exception.InvalidVerificationTokenException
import com.online_market_place.online_market_place.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.mapper.toUserResponse
import com.online_market_place.online_market_place.repository.user.UserRepository
import com.online_market_place.online_market_place.service.service_interface.AuthService
import com.online_market_place.online_market_place.service.service_interface.EmailService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID
@Service
class AuthServiceImplementation(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val emailService: EmailService,
    private val authenticationManager: AuthenticationManager


) : AuthService{

    /**
     * Registers a new user and sends a verification email.
     * @param request The user registration request containing email, username, password, and role.
     * @return A success message indicating that the user has been registered.
     * @throws Exception if the email is already in use or if there is an error sending the verification email.
     */

    override fun register(request: UserRegisterRequest): String {
        // Check if email already exists
        if (userRepository.existsByEmail(request.email)) {
            throw  EmailConflictException("Email ${request.email} is already registered")
        }

        // Generate verification token
        val verificationToken = UUID.randomUUID().toString()

        // Create user entity
        val user = UserEntity(
            email = request.email,
            username = request.username,
            password = passwordEncoder.encode(request.password),
            enabled = false,  // Start with disabled account
            role = request.role,
            verificationToken = verificationToken,
            tokenExpiryDate = LocalDateTime.now().plusHours(24)  // Token valid for 24 hours
        )

        // Save user to repository
        val savedUser = userRepository.save(user)

        // Send verification email
        try {
            emailService.sendVerificationEmail(savedUser.email, verificationToken)
        } catch (e: Exception) {
            // Log error but don't fail registration
            println("Unable to register")
        }

        return "User registered successfully. Please check your email for verification."
    }


    /**
     * Verifies the email of the user using the verification token.
     * @param token
     */

    override fun verifyEmail(token: String): String {

        val user = userRepository.findByVerificationToken(token)
            ?: throw InvalidVerificationTokenException("The verification token is invalid or has expired")

        // Check if token is expired
        if (user.tokenExpiryDate?.isBefore(LocalDateTime.now()) == true) {
            throw InvalidVerificationTokenException("Verification token has expired")
        }

        // Update user to enabled
        user.enabled = true
        user.verificationToken = null  // Clear the token after verification
        user.tokenExpiryDate = null  // Clear the expiry date

        // Save updated user to repository
        userRepository.save(user)

        return "Email verified successfully. You can now log in."

    }

    override fun login(request: LoginRequest): AuthResponse {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        var user : UserEntity = userRepository.findByEmail(request.email)
            ?: throw ResourceNotFoundException("User with these credential , not found")
        var userdetails: UserDetails = authentication.principal as UserDetails
        var jwtToken: String = jwtUtil.generateToken(userdetails)

        return AuthResponse(
            token = jwtToken,
            user = user.toUserResponse()
        )
    }





}