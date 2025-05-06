package com.online_market_place.online_market_place.order.controllers

import com.online_market_place.online_market_place.common.ApiResponse
import com.online_market_place.online_market_place.common.annotations.IsAdminOrSellerOrCustomer
import com.online_market_place.online_market_place.order.dto.OrderCreateDTO
import com.online_market_place.online_market_place.order.dto.OrderStatusUpdateDTO
import com.online_market_place.online_market_place.order.dto.OrderUpdateDTO
import com.online_market_place.online_market_place.order.services.OrderService
import com.online_market_place.online_market_place.product.enums.ProductOrderStatus
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/orders")

class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    @Operation(summary = "Create a new order")
    @IsAdminOrSellerOrCustomer
    fun createOrder(@Valid @RequestBody order: OrderCreateDTO.Input): ResponseEntity<OrderCreateDTO.Output> {
        val createdOrder = orderService.createOrder(order)
        return ResponseEntity.ok(createdOrder)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    @IsAdminOrSellerOrCustomer
    fun getOrderById(@PathVariable id: Long): ResponseEntity<OrderCreateDTO.Output> {
        val order = orderService.getOrderById(id)
        return ResponseEntity.ok(order)
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    @IsAdminOrSellerOrCustomer
    fun getAllOrders(): ResponseEntity<List<OrderCreateDTO.Output>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity.ok(orders)
    }

    @PutMapping("/status")
    @Operation(summary = "Update order status")
    @IsAdminOrSellerOrCustomer
    fun updateOrderStatus(@Valid @RequestBody statusUpdate: OrderStatusUpdateDTO.Input): ResponseEntity<OrderStatusUpdateDTO.Output> {
        val updatedOrder = orderService.updateOrderStatus(statusUpdate)
        return ResponseEntity.ok(updatedOrder)
    }


    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status")
    fun getOrdersByStatus(@Valid @PathVariable status: ProductOrderStatus): ResponseEntity<List<OrderCreateDTO.Output>> {
        val orders = orderService.getOrdersByStatus(status.name)
        return ResponseEntity.ok(orders)
    }

    @PutMapping
    @Operation(summary = "Update an existing order")
    fun updateOrder(@Valid @RequestBody updateRequest: OrderUpdateDTO.Input): ResponseEntity<OrderCreateDTO.Output> {
        val updatedOrder = orderService.updateOrder(updateRequest)
        return ResponseEntity.ok(updatedOrder)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        val apiResponse = ApiResponse(
            message = "Order with ID $id deleted successfully",
            success = true
        )

        return ResponseEntity.ok(apiResponse)
    }
}
