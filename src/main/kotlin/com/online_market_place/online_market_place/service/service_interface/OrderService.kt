package com.online_market_place.online_market_place.service.service_interface

import com.online_market_place.online_market_place.dto.order.OrderCreateRequest
import com.online_market_place.online_market_place.dto.order.OrderResponse
import com.online_market_place.online_market_place.dto.order.OrderStatusUpdate
import com.online_market_place.online_market_place.dto.order.OrderUpdateRequest

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