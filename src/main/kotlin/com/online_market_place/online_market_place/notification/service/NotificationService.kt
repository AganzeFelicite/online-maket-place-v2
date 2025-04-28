package com.online_market_place.online_market_place.notification.service

import com.online_market_place.online_market_place.notification.dto.CreateNotificationRequest
import com.online_market_place.online_market_place.notification.dto.NotificationResponse


interface NotificationService {
    fun createNotification(request: CreateNotificationRequest): NotificationResponse
    fun getUnreadNotifications(recipient: String): List<NotificationResponse>
    fun markAsRead(notificationId: Long): NotificationResponse
}
