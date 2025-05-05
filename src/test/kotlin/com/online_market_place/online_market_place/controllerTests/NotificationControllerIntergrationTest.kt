package com.online_market_place.online_market_place.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.common.config.security.SecurityConfig
import com.online_market_place.online_market_place.notification.dto.CreateNotificationDTO
import com.online_market_place.online_market_place.notification.entities.NotificationEntity
import com.online_market_place.online_market_place.notification.enums.NotificationType
import com.online_market_place.online_market_place.notification.repositories.NotificationRepository
import com.online_market_place.online_market_place.test_config.TestConfig
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
class NotificationControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @BeforeEach
    fun setup() {
        notificationRepository.deleteAll()
    }

    @Test
    @WithMockUser(roles = ["CUSTOMER"])
    fun `should create a notification when authorized`() {
        val input = CreateNotificationDTO.Input(
            title = "Test Notification",
            message = "This is a test message",
            recipient = "testuser@example.com",
            type = NotificationType.EMAIL,

            )

        val result = mockMvc.perform(
            post("/api/v2.0/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
    }

    @Test
    fun `should return 403 when unauthenticated user tries to create notification`() {
        val input = CreateNotificationDTO.Input(
            title = "Test Notification",
            message = "This is a test message",
            recipient = "testuser@example.com",
            type = NotificationType.EMAIL,
        )

        val result = mockMvc.perform(
            post("/api/v2.0/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
        )
            .andExpect(status().isForbidden)
            .andReturn()

        assertEquals(HttpStatus.FORBIDDEN.value(), result.response.status)
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should get unread notifications for recipient`() {
        val recipient = "recipient@example.com"
        val notification = NotificationEntity(
            title = "Unread",
            message = "Please read me",
            recipient = recipient,
            type = NotificationType.EMAIL,
            isRead = false
        )
        notificationRepository.save(notification)

        val result = mockMvc.perform(get("/api/v2.0/notifications/unread/$recipient"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should mark notification as read`() {
        val recipient = "recipient@example.com"
        val notification = NotificationEntity(
            title = "Unread",
            message = "Please read me",
            recipient = recipient,
            type = NotificationType.EMAIL,
            isRead = false
        )
        val saved = notificationRepository.save(notification)

        val result = mockMvc.perform(patch("/api/v2.0/notifications/${saved.id}/read"))
            .andExpect(status().isOk)
            .andReturn()

        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)

        val updated = notificationRepository.findById(saved.id).get()
        assertEquals(true, updated.isRead)
    }
}
