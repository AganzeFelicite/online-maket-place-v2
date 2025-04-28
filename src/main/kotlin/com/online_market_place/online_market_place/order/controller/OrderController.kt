package com.online_market_place.online_market_place.order.controller

import com.online_market_place.online_market_place.common.ApiResponse
import com.online_market_place.online_market_place.common.annotation.IsAdminOrSellerOrCustomer
import com.online_market_place.online_market_place.order.dto.OrderCreateRequest
import com.online_market_place.online_market_place.order.dto.OrderResponse
import com.online_market_place.online_market_place.order.dto.OrderStatusUpdate
import com.online_market_place.online_market_place.order.dto.OrderUpdateRequest
import com.online_market_place.online_market_place.order.service.OrderService
import com.online_market_place.online_market_place.product.enum_.ProductOrderStatus
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/orders")
@SecurityRequirement(name = "bearerAuth") // this is just for swagger
@Suppress("unused") // Suppress unused warning for the class since this is a controller class
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    @Operation(summary = "Create a new order")
    @IsAdminOrSellerOrCustomer
    fun createOrder(@Valid @RequestBody order: OrderCreateRequest): ResponseEntity<OrderResponse> {
        val createdOrder = orderService.createOrder(order)
        return ResponseEntity.ok(createdOrder)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    @IsAdminOrSellerOrCustomer
    fun getOrderById(@PathVariable id: Long): ResponseEntity<OrderResponse> {
        val order = orderService.getOrderById(id)
        return ResponseEntity.ok(order)
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    @IsAdminOrSellerOrCustomer
    fun getAllOrders(): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity.ok(orders)
    }

    @PutMapping("/status")
    @Operation(summary = "Update order status")
    @IsAdminOrSellerOrCustomer
    fun updateOrderStatus(@Valid @RequestBody statusUpdate: OrderStatusUpdate): ResponseEntity<OrderResponse> {
        val updatedOrder = orderService.updateOrderStatus(statusUpdate)
        return ResponseEntity.ok(updatedOrder)
    }


    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status")
    fun getOrdersByStatus(@Valid @PathVariable status: ProductOrderStatus): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getOrdersByStatus(status.name)
        return ResponseEntity.ok(orders)
    }

    @PutMapping
    @Operation(summary = "Update an existing order")
    fun updateOrder(@Valid @RequestBody updateRequest: OrderUpdateRequest): ResponseEntity<OrderResponse> {
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
