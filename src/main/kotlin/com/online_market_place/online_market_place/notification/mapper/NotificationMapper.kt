package com.online_market_place.online_market_place.notification.mapper

import com.online_market_place.online_market_place.notification.dto.NotificationResponse
import com.online_market_place.online_market_place.notification.entity.NotificationEntity

fun toNotificationResponse(notification: NotificationEntity): NotificationResponse {
    return NotificationResponse(
        id = notification.id,
        title = notification.title,
        message = notification.message,
        recipient = notification.recipient,
        type = notification.type,
        read = notification.read,
        createdAt = notification.createdAt
    )
}