package com.online_market_place.online_market_place.controllerTests
import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.common.config.security.SecurityConfig
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
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig::class)
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
       userRepository.deleteAll()
    }


    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should return all users when GET request is made by Admin`() {

        mockMvc.perform(get("/api/v2.0/users"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<Int>(userService.getAllUsers().size)))

    }

    @Test
    fun `should return 403 when GET request is made by non-admin`() {

        mockMvc.perform(get("/api/v2.0/users"))
            .andExpect(status().isForbidden)
    }



    @Test
    @WithMockUser(roles = ["ADMIN", "SELLER", "CUSTOMER"])
    fun `should return user by ID when authorized`() {
        val user = UserEntity(
            email = "test@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            role = setOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val userId = newUser.id
        mockMvc.perform(get("/api/v2.0/users/$userId"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `should return 403 when unauthorized user tries to get user by ID`() {
        val userId = 1L
        mockMvc.perform(get("/api/v2.0/users/$userId"))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = ["CUSTOMER"])
    fun `should update user when authorized`() {
        val user = UserEntity(
            email = "testuser@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            role = setOf(UserRole.CUSTOMER),
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
                role = setOf(UserRole.CUSTOMER, UserRole.SELLER)
            )
        }

        mockMvc.perform(put("/api/v2.0/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = ["CUSTOMER"])
    fun `should return all orders for user`() {
        val user = UserEntity(
            email = "testuser@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = true,
            role = setOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        userRepository.save(user)
        mockMvc.perform(get("/api/v2.0/users/orders"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            role = setOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        userRepository.save(user)

        val status = "COMPLETED"
        mockMvc.perform(get("/api/v2.0/users/orders/status/$status"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }


    @Test
    @WithMockUser(roles = ["ADMIN", "SELLER", "CUSTOMER"])
    fun `should return user orders by user ID`() {
        val user = UserEntity(
            email = "test@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            role = setOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val userId = newUser.id
        mockMvc.perform(get("/api/v2.0/users/$userId/orders"))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should delete user when authorized`() {
        val user = UserEntity(
            email = "test@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            role = setOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val userId = newUser.id
        mockMvc.perform(delete("/api/v2.0/users/$userId"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    @WithMockUser(roles = ["CUSTOMER"])
    fun `should return 403 when unauthorized user tries to delete user`() {
        val user = UserEntity(
            email = "test@example.com",
            username = "testuser",
            password = "Test@123",
            enabled = false,
            role = setOf(UserRole.CUSTOMER),
            verificationToken = null,
            tokenExpiryDate = null
        )
        val newUser = userRepository.save(user)
        val userId = newUser.id
        mockMvc.perform(delete("/api/v2.0/users/$userId"))
            .andExpect(status().isForbidden)
    }



}