package com.online_market_place.online_market_place.notification.dto

import com.online_market_place.online_market_place.notification.enums.NotificationType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

sealed class CreateNotificationDTO {

    data class Input(

        @field:NotBlank(message = "Title is required")
        val title: String,

        @field:NotBlank(message = "Message is required")
        val message: String,

        @field:NotBlank(message = "Recipient is required")
        val recipient: String,

        @field:NotNull(message = "Notification type is required")
        val type: NotificationType
    )


    data class Output(
        val id: Long,
        val title: String,
        val message: String,
        val recipient: String,
        val type: NotificationType,
        val read: Boolean,
        val createdAt: LocalDateTime
    )
}
