package com.online_market_place.online_market_place.notification.repositories

import com.online_market_place.online_market_place.notification.entities.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<NotificationEntity, Long> {
    fun findByRecipientAndIsRead(recipient: String, read: Boolean): List<NotificationEntity>

}