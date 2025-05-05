package com.online_market_place.online_market_place.order.services

import com.online_market_place.online_market_place.order.dto.OrderCreateDTO
import com.online_market_place.online_market_place.order.dto.OrderMessage
import com.online_market_place.online_market_place.order.dto.OrderStatusUpdateDTO
import com.online_market_place.online_market_place.order.dto.OrderUpdateDTO

interface OrderService {
    fun createOrder(order: OrderCreateDTO.Input): OrderCreateDTO.Output

    fun getOrderById(orderId: Long): OrderCreateDTO.Output

    fun getAllOrders(): List<OrderCreateDTO.Output>

    fun updateOrderStatus(order: OrderStatusUpdateDTO.Input): OrderStatusUpdateDTO.Output

    fun getOrdersByUserId(userId: Long): List<OrderCreateDTO.Output>

    fun getOrdersByProductId(productId: Long): List<OrderCreateDTO.Output>

    fun getOrdersByStatus(status: String): List<OrderCreateDTO.Output>

    fun updateOrder(order: OrderUpdateDTO.Input): OrderCreateDTO.Output

    fun deleteOrder(orderId: Long): String
    fun failOrder(order: OrderCreateDTO.Output, message: OrderMessage, reason: String)

    fun confirmedProduct(order: OrderCreateDTO.Output, message: OrderMessage)

}