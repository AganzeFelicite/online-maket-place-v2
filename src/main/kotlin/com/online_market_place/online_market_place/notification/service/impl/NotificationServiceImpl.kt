package com.online_market_place.online_market_place.notification.service.impl

import com.online_market_place.online_market_place.common.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.notification.dto.CreateNotificationRequest
import com.online_market_place.online_market_place.notification.dto.NotificationResponse
import com.online_market_place.online_market_place.notification.entity.NotificationEntity
import com.online_market_place.online_market_place.notification.mapper.toNotificationResponse
import com.online_market_place.online_market_place.notification.repository.NotificationRepository
import com.online_market_place.online_market_place.notification.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
// TODO remove these suppressions
@Suppress("unused")
class NotificationServiceImpl @Autowired constructor(
    private val notificationRepository: NotificationRepository
) : NotificationService {

    override fun createNotification(request: CreateNotificationRequest): NotificationResponse {
        val notification = NotificationEntity(
            title = request.title,
            message = request.message,
            recipient = request.recipient,
            type = request.type
        )
        val savedNotification = notificationRepository.save(notification)
        return toNotificationResponse(savedNotification)
    }

    override fun getUnreadNotifications(recipient: String): List<NotificationResponse> {
        val notifications = notificationRepository.findByRecipientAndIsRead(recipient, false)
        return notifications.map { toNotificationResponse(it) }
    }

    override fun markAsRead(notificationId: Long): NotificationResponse {
        val notification =
            notificationRepository.findById(notificationId)
                .orElseThrow { ResourceNotFoundException("Notification not found") }
        val updatedNotification = notification.copy(isRead = true)
        val savedNotification = notificationRepository.save(updatedNotification)
        return toNotificationResponse(savedNotification)
    }


}
