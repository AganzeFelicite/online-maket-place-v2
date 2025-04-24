//package com.online_market_place.online_market_place.service_test
//import UserResponse
//import com.online_market_place.online_market_place.auth.dto.AuthResponse
//import com.online_market_place.online_market_place.auth.dto.LoginRequest
//import com.online_market_place.online_market_place.user.dto.UserRegisterRequest
//import com.online_market_place.online_market_place.user.enum_.UserRole
//import com.online_market_place.online_market_place.user.entity.UserEntity
//import com.online_market_place.online_market_place.auth.exception.EmailConflictException
//import com.online_market_place.online_market_place.user.repository.UserRepository
//import com.online_market_place.online_market_place.common.config.security.JwtUtil
//import com.online_market_place.online_market_place.auth.service.impl.AuthServiceImpl
//import com.online_market_place.online_market_place.notification.service.EmailService
//import io.mockk.*
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.*
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.crypto.password.PasswordEncoder
//import java.time.LocalDateTime
//
//class AuthServiceImplTest {
//
//    private lateinit var userRepository: UserRepository
//    private lateinit var passwordEncoder: PasswordEncoder
//    private lateinit var jwtUtil: JwtUtil
//    private lateinit var emailService: EmailService
//    private lateinit var authenticationManager: AuthenticationManager
//    private lateinit var authService: AuthServiceImpl
//
//    @BeforeEach
//    fun setup() {
//        userRepository = mockk()
//        passwordEncoder = mockk()
//        jwtUtil = mockk()
//        emailService = mockk()
//        authenticationManager = mockk()
//        authService = AuthServiceImpl(userRepository, passwordEncoder, jwtUtil, emailService, authenticationManager)
//    }
//
//    @Test
//    fun `register should save user and send verification email`() {
//        val request = UserRegisterRequest("aganze@email.com", "Aganze", "password123", setOf(UserRole.USER))
//        every { userRepository.existsByEmail(request.email) } returns false
//        every { passwordEncoder.encode(request.password) } returns "hashedPassword"
//        every { userRepository.save(any()) } returnsArgument 0
//        every { emailService.sendVerificationEmail("", any()) } just Runs
//
//        val response = authService.register(request)
//
//        assertTrue(response.contains("User registered successfully"))
//        verify(exactly = 1) { userRepository.save(any()) }
//        verify { emailService.sendVerificationEmail(match { it == request.email }, any()) }
//    }
//
//    @Test
//    fun `register should throw EmailConflictException when email exists`() {
//        val request = UserRegisterRequest("aganzefelicite07@email.com", "Aganze", "password123", setOf(UserRole.USER))
//        every { userRepository.existsByEmail(request.email) } returns true
//
//        val exception = assertThrows<EmailConflictException> {
//            authService.register(request)
//        }
//        assertEquals("Email ${request.email} is already registered", exception.message)
//    }
//
//    @Test
//    fun `verifyToken should enable user and clear token`() {
//        val user = UserEntity(
//            id = 1,
//            email = "test@email.com",
//            username = "user",
//            password = "pass",
//            enabled = false,
//            verificationToken = "token123",
//            tokenExpiryDate = LocalDateTime.now().plusHours(1),
//            role = setOf(UserRole.USER)
//        )
//
//        every { userRepository.findByVerificationToken("token123") } returns user
//        every { userRepository.save(any()) } returnsArgument 0
//
//        val response = authService.verifyToken("token123")
//
//        assertEquals("Email verified successfully. You can now log in.", response)
//        assertTrue(user.enabled)
//        assertNull(user.verificationToken)
//    }
//
//    @Test
//    fun `login should authenticate user and return token`() {
//        val request = LoginRequest("test@email.com", "password")
//        val user = UserEntity(
//            id = 1,
//            email = request.email,
//            username = "Test",
//            password = "hashedPassword",
//            enabled = true,
//            role = setOf(UserRole.USER),
//            verificationToken = null,
//            tokenExpiryDate = null
//        )
//        val userDetails = mockk<UserDetails>()
//        val auth = mockk<Authentication>()
//
//        every { authenticationManager.authenticate(any()) } returns auth
//        every { auth.principal } returns userDetails
//        every { jwtUtil.generateToken(userDetails) } returns "mocked-jwt-token"
//        every { userRepository.findByEmail(request.email) } returns user
//
//        val response: AuthResponse = authService.login(request)
//        val userResponse = response.user as UserResponse
//
//        assertEquals("mocked-jwt-token", response.token)
//        assertEquals(user.email, userResponse.email)
//    }
//}
