package com.online_market_place.online_market_place.notification.dto

import com.online_market_place.online_market_place.notification.enum_.NotificationType

data class NotificationResponse(
    val id: Long,
    val title: String,
    val message: String,
    val recipient: String,
    val type: NotificationType,
    val read: Boolean,
    val createdAt: Long
)
