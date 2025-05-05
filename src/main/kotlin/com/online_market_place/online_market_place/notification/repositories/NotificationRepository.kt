package com.online_market_place.online_market_place.notification.repositories

import com.online_market_place.online_market_place.notification.entities.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface NotificationRepository : JpaRepository<NotificationEntity, Long> {
    fun findByRecipientAndIsRead(recipient: String, read: Boolean): List<NotificationEntity>

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM notifications", nativeQuery = true)
    fun deleteAllPhysically()

}