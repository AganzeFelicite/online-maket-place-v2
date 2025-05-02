package com.online_market_place.online_market_place.notification.controllers

import com.online_market_place.online_market_place.common.annotations.IsAdminOrSellerOrCustomer
import com.online_market_place.online_market_place.notification.dto.CreateNotificationDTO
import com.online_market_place.online_market_place.notification.services.NotificationService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/notifications")
class NotificationController @Autowired constructor(
    private val notificationService: NotificationService
) {

    @PostMapping
    @Operation(summary = "Create a new notification")
    @IsAdminOrSellerOrCustomer
    fun createNotification(
        @RequestBody @Valid request: CreateNotificationDTO.Input
    ): CreateNotificationDTO.Output {
        return notificationService.createNotification(request)
    }

    @GetMapping("/unread/{recipient}")
    @Operation(summary = "Get unread notifications for a recipient")
    @IsAdminOrSellerOrCustomer
    fun getUnreadNotifications(@PathVariable recipient: String): List<CreateNotificationDTO.Output> {
        return notificationService.getUnreadNotifications(recipient)
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark a notification as read")
    @IsAdminOrSellerOrCustomer
    fun markAsRead(@PathVariable notificationId: Long): CreateNotificationDTO.Output {
        return notificationService.markAsRead(notificationId)
    }
}
