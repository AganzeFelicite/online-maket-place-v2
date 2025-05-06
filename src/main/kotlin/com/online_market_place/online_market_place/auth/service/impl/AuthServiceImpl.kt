package com.online_market_place.online_market_place.auth.service.impl
import com.online_market_place.online_market_place.auth.dto.AuthDTO
import com.online_market_place.online_market_place.auth.exception.EmailConflictException
import com.online_market_place.online_market_place.auth.exception.InvalidVerificationTokenException
import com.online_market_place.online_market_place.auth.service.AuthService
import com.online_market_place.online_market_place.common.config.security.JwtUtil
import com.online_market_place.online_market_place.notification.services.EmailService
import com.online_market_place.online_market_place.user.dto.UserCreateDTO
import com.online_market_place.online_market_place.user.entities.UserEntity
import com.online_market_place.online_market_place.user.mappers.UserMapper
import com.online_market_place.online_market_place.user.repositories.UserRepository
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val emailService: EmailService,
    private val authenticationManager: AuthenticationManager


) : AuthService {

    val log = KotlinLogging.logger {}

    @Transactional
    override fun register(request: UserCreateDTO.Input): UserCreateDTO.Output {
        if (userRepository.existsByEmail(request.email)) {
            throw EmailConflictException("Email ${request.email} is already registered")
        }

        val verificationToken = UUID.randomUUID().toString()
        val userEntity = createUserEntity(request, verificationToken)
        val savedUser = userRepository.save(userEntity)

        // Move email sending inside the try block
        try {
            emailService.sendVerificationEmail(savedUser.email, verificationToken)
        } catch (ex: Exception) {
            log.error(ex) { "Email sending failed for ${savedUser.email}" }
            throw IllegalStateException("Registration failed: Unable to send verification email.")
        }

        return UserCreateDTO.Output(
            success = true,
            message = "User registered successfully. Please check your email for verification.",
            data = UserMapper().map(savedUser, false)
        )
    }


    private fun createUserEntity(request: UserCreateDTO.Input, token: String): UserEntity {
        return UserEntity(
            email = request.email,
            username = request.username,
            password = passwordEncoder.encode(request.password),
            enabled = false,
            roles = request.role,
            verificationToken = token,
            tokenExpiryDate = LocalDateTime.now().plusHours(24)
        )
    }


    override fun verifyToken(token: String): String {

        val user = userRepository.findByVerificationToken(token)



        if (user.isTokenExpired()) {
            throw InvalidVerificationTokenException()
        }

        user.verifyEmail(token)
        userRepository.save(user)

        return "Email verified successfully. You can now log in."

    }

    override fun login(request: AuthDTO.Input): AuthDTO.Out {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val user: UserEntity = userRepository.findByEmail(request.email)
        val userDetails: UserDetails = authentication.principal as UserDetails
        val jwtToken: String = jwtUtil.generateToken(userDetails)

        return AuthDTO.Out(
            token = jwtToken,
            user = UserMapper().map(user, false),
        )
    }

}