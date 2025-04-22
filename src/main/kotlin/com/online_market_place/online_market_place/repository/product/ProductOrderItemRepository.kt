package com.online_market_place.online_market_place.repository.product

import com.online_market_place.online_market_place.entiy.order.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductOrderItemRepository : JpaRepository<OrderItemEntity, Long>