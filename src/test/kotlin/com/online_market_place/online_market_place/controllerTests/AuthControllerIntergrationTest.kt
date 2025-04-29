package com.online_market_place.online_market_place.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.auth.dto.LoginRequest
import com.online_market_place.online_market_place.common.config.security.JwtUtil
import com.online_market_place.online_market_place.common.config.security.SecurityConfig
import com.online_market_place.online_market_place.test_config.TestConfig
import com.online_market_place.online_market_place.user.entity.UserEntity
import com.online_market_place.online_market_place.user.enum_.UserRole
import com.online_market_place.online_market_place.user.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc

@Import(SecurityConfig::class, TestConfig::class)

class AuthControllerIntergrationTest {


    @Autowired  private lateinit var mockMvc: MockMvc
    @Autowired  private lateinit var objectMapper: ObjectMapper
    @Autowired  private lateinit var userRepository: UserRepository
    @Autowired  private lateinit var passwordEncoder: PasswordEncoder
    @Autowired  private lateinit var jwtUtil: JwtUtil




    private lateinit var jwtToken: String
    private lateinit var email: String
    private val password = "Password@123"

    @BeforeEach
    fun setup() {
        // Clear DB to avoid conflict
        userRepository.deleteAll()

        // Create a user
        email = "testuser@example.com"
        val user = UserEntity(
            email = email,
            username = "testuser",
            password = passwordEncoder.encode(password),
            enabled = true,
            role = setOf(UserRole.CUSTOMER),
            verificationToken = "someVerificationToken",
            tokenExpiryDate = LocalDateTime.now().plusHours(1),
        )
        userRepository.save(user)
        val userDetails = org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            user.enabled,
            true,
            true,
            true,
            user.role.map { org.springframework.security.core.GrantedAuthority { it.name } }
        )



        // Generate a valid JWT token
        jwtToken = jwtUtil.generateToken(userDetails)

    }

    @Test
    fun `should register a new user`() {

        val registerRequest = mapOf(
            "email" to "newuser@example.com",
            "username" to "newuser",
            "password" to "NewPassword@123",
            "role" to setOf("CUSTOMER")
        )

        // TODO Extract the result and perform assertions on it
        mockMvc.perform(
            post("/api/v2.0/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk)
            .andExpect(content().string(org.hamcrest.Matchers.containsString("User registered successfully. Please check your email for verification")))
    }

    @Test
    fun `should login user`() {
        val loginRequest = LoginRequest(
            email = email,
            password = password
        )

        mockMvc.perform(post("/api/v2.0/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.user").exists())
    }

    @Test
    fun `should verify email with valid token`() {
        val token = "someVerificationToken" // Replace with a real one if needed.

        mockMvc.perform(get("/api/v2.0/auth/verify-email")
            .param("token", token))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").exists())
    }

    @Test
    fun `should logout user`() {
        mockMvc.perform(post("/api/v2.0/auth/logout")
            .header("Authorization", "Bearer $jwtToken"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Logged out successfully"))
    }
}
