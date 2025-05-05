package com.online_market_place.online_market_place.order.repositories

import com.online_market_place.online_market_place.order.entities.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface OrderItemRepository : JpaRepository<OrderItemEntity, Long> {
    fun findByOrderId(orderId: Long): List<OrderItemEntity>

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM order_items", nativeQuery = true)
    fun deleteAllPhysically()
}
