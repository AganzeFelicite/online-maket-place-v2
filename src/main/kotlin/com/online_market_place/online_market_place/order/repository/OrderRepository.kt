package com.online_market_place.online_market_place.order.repository

import com.online_market_place.online_market_place.order.entity.OrderEntity
import com.online_market_place.online_market_place.product.ProductOrderStatus
import com.online_market_place.online_market_place.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderEntity, Long> {
    fun findAllByStatus(enumStatus: ProductOrderStatus): List<OrderEntity>
    fun findAllByUser(user: UserEntity?): List<OrderEntity>
}