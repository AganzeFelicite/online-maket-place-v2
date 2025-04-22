package com.online_market_place.online_market_place.repository.product

import com.online_market_place.online_market_place.entiy.enum_.ProductOrderStatus
import com.online_market_place.online_market_place.entiy.order.OrderEntity
import com.online_market_place.online_market_place.entiy.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductOrderRepository : JpaRepository<OrderEntity, Long> {
    abstract fun findAllByStatus(enumStatus: ProductOrderStatus): List<OrderEntity>
    abstract fun findAllByUser(user: UserEntity?): List<OrderEntity>
}