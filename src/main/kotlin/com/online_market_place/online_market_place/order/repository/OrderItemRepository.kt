package com.online_market_place.online_market_place.order.repository

import com.online_market_place.online_market_place.order.entity.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemRepository : JpaRepository<OrderItemEntity, Long>