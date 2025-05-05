package com.online_market_place.online_market_place.notification.entities

import com.online_market_place.online_market_place.common.base.BaseEntity
import com.online_market_place.online_market_place.notification.enums.NotificationType
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction


@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET deleted_at = now() WHERE id=?")
@SQLRestriction("deleted_at is null")
data class NotificationEntity(
    var title: String,

    var message: String,

    var recipient: String,

    var type: NotificationType,

    var isRead: Boolean = false,

    ) : BaseEntity() {

    fun markAsRead() {
        this.isRead = true
    }

    fun updateNotificationDetails(
        title: String? = null,
        message: String? = null,
        recipient: String? = null,
        type: NotificationType? = null
    ) {
        title?.let { this.title = it }
        message?.let { this.message = it }
        recipient?.let { this.recipient = it }
        type?.let { this.type = it }
    }
}


