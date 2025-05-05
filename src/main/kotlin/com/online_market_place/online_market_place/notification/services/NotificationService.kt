package com.online_market_place.online_market_place.notification.services

import com.online_market_place.online_market_place.notification.dto.CreateNotificationDTO


interface NotificationService {
    fun createNotification(request: CreateNotificationDTO.Input): CreateNotificationDTO.Output
    fun getUnreadNotifications(recipient: String): List<CreateNotificationDTO.Output>
    fun markAsRead(notificationId: Long): CreateNotificationDTO.Output

}
