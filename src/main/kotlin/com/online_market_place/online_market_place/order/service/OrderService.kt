package com.online_market_place.online_market_place.order.service

import com.online_market_place.online_market_place.order.dto.OrderCreateRequest
import com.online_market_place.online_market_place.order.dto.OrderResponse
import com.online_market_place.online_market_place.order.dto.OrderStatusUpdate
import com.online_market_place.online_market_place.order.dto.OrderUpdateRequest

interface OrderService {
    fun createOrder(order: OrderCreateRequest): OrderResponse

    fun getOrderById(orderId: Long): OrderResponse

    fun getAllOrders(): List<OrderResponse>

    fun updateOrderStatus(order: OrderStatusUpdate): OrderResponse

    fun getOrdersByUserId(userId: Long): List<OrderResponse>

    fun getOrdersByProductId(productId: Long): List<OrderResponse>

    fun getOrdersByStatus(status: String): List<OrderResponse>

    fun updateOrder(order: OrderUpdateRequest): OrderResponse

    fun deleteOrder(orderId: Long): String

}