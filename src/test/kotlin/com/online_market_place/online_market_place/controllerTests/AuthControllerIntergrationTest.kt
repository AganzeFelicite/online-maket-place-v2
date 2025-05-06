package com.online_market_place.online_market_place.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.auth.dto.AuthDTO
import com.online_market_place.online_market_place.common.config.security.JwtUtil
import com.online_market_place.online_market_place.test_config.TestConfig
import com.online_market_place.online_market_place.user.entities.UserEntity
import com.online_market_place.online_market_place.user.enums.UserRole
import com.online_market_place.online_market_place.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime
import kotlin.test.assertEquals


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig::class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerIntergrationTest {


    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var jwtUtil: JwtUtil


    // Create a TestUtils class or extension function

    private lateinit var jwtToken: String
    private lateinit var email: String
    private val password = "Password@123"

    @BeforeEach
    fun setup() {
        // Clear DB to avoid conflict
        userRepository.deleteAllPhysically()

        // Create a user
        email = "testuser@example.com"
        val user = UserEntity(
            email = email,
            username = "testuser",
            password = passwordEncoder.encode(password),
            enabled = true,
            roles = mutableListOf(UserRole.CUSTOMER),
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
            user.roles.map { org.springframework.security.core.GrantedAuthority { it.name } }
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

        val result = mockMvc.perform(
            post("/api/v2.0/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andReturn()

        assertEquals(HttpStatus.OK.value(), result.response.status)


    }

    @Test
    fun `should login user`() {
        val loginRequest = AuthDTO.Input(
            email = email,
            password = password
        )

        val result = mockMvc.perform(
            post("/api/v2.0/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andReturn()
        assertEquals(HttpStatus.OK.value(), result.response.status)

    }

    @Test
    fun `should verify email with valid token`() {
        val token = "someVerificationToken"

        val result = mockMvc.perform(
            get("/api/v2.0/auth/verify-email")
                .param("token", token)
        )
            .andReturn()
        val response = result.response.contentAsString
        assertEquals(HttpStatus.OK.value(), result.response.status)
        assertEquals(true, response.contains("success"))
        assertEquals(true, response.contains("message"))
    }

    @Test
    fun `should logout user`() {
        val result = mockMvc.perform(
            post("/api/v2.0/auth/logout")
                .header("Authorization", "Bearer $jwtToken")
        )
            .andReturn()
        val response = result.response.contentAsString
        assertEquals(HttpStatus.OK.value(), result.response.status)
        assertEquals(true, response.contains("success"))
        assertEquals(true, response.contains("Logged out successfully"))


    }
}
