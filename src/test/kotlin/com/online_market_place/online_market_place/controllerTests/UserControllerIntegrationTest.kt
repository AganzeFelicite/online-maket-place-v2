package com.online_market_place.online_market_place.controllerTests
import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.common.config.security.SecurityConfig
import com.online_market_place.online_market_place.test_config.TestConfig
import com.online_market_place.online_market_place.user.dto.UserUpdateDTO
import com.online_market_place.online_market_place.user.entities.UserEntity
import com.online_market_place.online_market_place.user.enums.UserRole
import com.online_market_place.online_market_place.user.repositories.UserRepository
import com.online_market_place.online_market_place.user.services.UserService
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertEquals


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(SecurityConfig::class, TestConfig::class)
class UserControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper
    @BeforeEach
    fun setup() {
        userRepository.deleteAllPhysically()
    }


    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should return all users when GET request is made by Admin`() {

        val result = mockMvc.perform(get("/api/v2.0/users"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<Int>(userService.getAllUsers().size)))
            .andReturn()
        val response = result.response
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
        assertEquals(HttpStatus.OK.value(), response.status)

    }

    @Test
    fun `should return 403 when GET request is made by non-admin`() {

        val result = mockMvc.perform(get("/api/v2.0/users"))
            .andExpect(status().isForbidden)
            .andReturn()

        val response = result.response
        assertEquals(HttpStatus.FORBIDDEN.value(), response.status)
    }



    @Test
    @WithMockUser(roles = ["ADMIN", "SELLER", "CUSTOMER"])
    fun `should return user by ID when authorized`() {
        val user = UserEntity(
            email = "test@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            roles = mutableListOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val userId = newUser.id
        val result = mockMvc.perform(get("/api/v2.0/users/$userId"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)


    }

    @Test
    fun `should return 403 when unauthorized user tries to get user by ID`() {
        val userId = 1L
        val result = mockMvc.perform(get("/api/v2.0/users/$userId"))
            .andExpect(status().isForbidden)
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.FORBIDDEN.value(), response.status)


    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = ["CUSTOMER"])
    fun `should update user when authorized`() {
        val user = UserEntity(
            email = "testuser@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            roles = mutableListOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val updateRequest = newUser.id.let {
            UserUpdateDTO.Input(
                id = it,
                username = "updatedUser",
                password = "NewPassword@123",
                enabled = true,
                role = mutableListOf(UserRole.CUSTOMER, UserRole.SELLER)
            )
        }

        val result = mockMvc.perform(
            put("/api/v2.0/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)

    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = ["CUSTOMER"])
    fun `should return all orders for user`() {
        val user = UserEntity(
            email = "testuser@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = true,
            roles = mutableListOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        userRepository.save(user)
        val result = mockMvc.perform(get("/api/v2.0/users/orders"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
        assertEquals("[]", response.contentAsString)
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = ["CUSTOMER"])
    fun `should return user orders by status`() {
        // Setup: Create and save a user matching the MockUser
        val user = UserEntity(
            email = "testuser@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = true,
            roles = mutableListOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        userRepository.save(user)

        val status = "COMPLETED"
        val results = mockMvc.perform(get("/api/v2.0/users/orders/status/$status"))
            .andExpect(status().isOk)
            .andReturn()
        val response = results.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
        assertEquals("[]", response.contentAsString)
    }


    @Test
    @WithMockUser(roles = ["ADMIN", "SELLER", "CUSTOMER"])
    fun `should return user orders by user ID`() {
        val user = UserEntity(
            email = "test@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            roles = mutableListOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val userId = newUser.id
        val result = mockMvc.perform(get("/api/v2.0/users/$userId/orders"))
            .andExpect(status().isOk)
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
        assertEquals("[]", response.contentAsString)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should delete user when authorized`() {
        val user = UserEntity(
            email = "test@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            roles = mutableListOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val userId = newUser.id
        val result = mockMvc.perform(delete("/api/v2.0/users/$userId"))
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)

    }

    @Test
    @WithMockUser(roles = ["CUSTOMER"])
    fun `should return 403 when unauthorized user tries to delete user`() {
        val user = UserEntity(
            email = "test@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            roles = mutableListOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val userId = newUser.id
        val result = mockMvc.perform(delete("/api/v2.0/users/$userId"))
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.FORBIDDEN.value(), response.status)

    }



}