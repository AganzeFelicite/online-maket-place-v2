package com.online_market_place.online_market_place.order.repositories

import com.online_market_place.online_market_place.order.entities.OrderEntity
import com.online_market_place.online_market_place.product.enums.ProductOrderStatus
import com.online_market_place.online_market_place.user.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface OrderRepository : JpaRepository<OrderEntity, Long> {
    fun findAllByStatus(enumStatus: ProductOrderStatus): List<OrderEntity>
    fun findAllByUser(user: UserEntity?): List<OrderEntity>

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM orders", nativeQuery = true)
    fun deleteAllPhysically()
}