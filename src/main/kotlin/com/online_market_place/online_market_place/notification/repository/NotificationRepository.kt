package com.online_market_place.online_market_place.notification.repository

import com.online_market_place.online_market_place.notification.entity.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<NotificationEntity, Long> {
    fun findByRecipientAndRead(recipient: String, read: Boolean): List<NotificationEntity>

}