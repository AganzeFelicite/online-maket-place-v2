package com.online_market_place.online_market_place.notification.dto

import com.online_market_place.online_market_place.notification.enum_.NotificationType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateNotificationRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,

    @field:NotBlank(message = "Message is required")
    val message: String,

    @field:NotBlank(message = "Recipient is required")
    val recipient: String,

    @field:NotNull(message = "Notification type is required")
    val type: NotificationType
)
