package com.online_market_place.online_market_place.notification.services.impl

import com.online_market_place.online_market_place.common.exceptions.ResourceNotFoundException
import com.online_market_place.online_market_place.notification.dto.CreateNotificationDTO
import com.online_market_place.online_market_place.notification.entities.NotificationEntity
import com.online_market_place.online_market_place.notification.mappers.NotificationMapper
import com.online_market_place.online_market_place.notification.repositories.NotificationRepository
import com.online_market_place.online_market_place.notification.services.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service

class NotificationServiceImpl @Autowired constructor(
    private val notificationRepository: NotificationRepository,

    ) : NotificationService {

    @Transactional
    override fun createNotification(request: CreateNotificationDTO.Input): CreateNotificationDTO.Output {
        val notification = NotificationEntity(
            title = request.title,
            message = request.message,
            recipient = request.recipient,
            type = request.type
        )
        val savedNotification = notificationRepository.save(notification)
        return NotificationMapper().map(savedNotification)
    }

    override fun getUnreadNotifications(recipient: String): List<CreateNotificationDTO.Output> {
        val notifications = notificationRepository.findByRecipientAndIsRead(recipient, false)
        return notifications.map { NotificationMapper().map(it) }
    }

    @Transactional
    override fun markAsRead(notificationId: Long): CreateNotificationDTO.Output {
        val notification =
            notificationRepository.findById(notificationId)
                .orElseThrow { ResourceNotFoundException("Notification not found") }
        notification.markAsRead()
        val savedNotification = notificationRepository.save(notification)
        return NotificationMapper().map(savedNotification)
    }





}
