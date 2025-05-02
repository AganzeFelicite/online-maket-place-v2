package com.online_market_place.online_market_place.notification.mappers

import com.online_market_place.online_market_place.notification.dto.CreateNotificationDTO
import com.online_market_place.online_market_place.notification.entities.NotificationEntity

fun toNotificationResponse(notification: NotificationEntity): CreateNotificationDTO.Output {
    return CreateNotificationDTO.Output(
        id = notification.id,
        title = notification.title,
        message = notification.message,
        recipient = notification.recipient,
        type = notification.type,
        read = notification.isRead,
        createdAt = notification.createdAt
    )
}