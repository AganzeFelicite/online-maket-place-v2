package com.online_market_place.online_market_place.controller.order
import com.online_market_place.online_market_place.dto.order.OrderCreateRequest
import com.online_market_place.online_market_place.dto.order.OrderResponse
import com.online_market_place.online_market_place.dto.order.OrderStatusUpdate
import com.online_market_place.online_market_place.dto.order.OrderUpdateRequest
import com.online_market_place.online_market_place.entiy.enum_.ProductOrderStatus
import com.online_market_place.online_market_place.service.service_interface.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/orders")

@SecurityRequirement(name = "bearerAuth")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    @Operation(summary = "Create a new order")
    fun createOrder(@Valid @RequestBody order: OrderCreateRequest): ResponseEntity<OrderResponse> {
        val createdOrder = orderService.createOrder(order)
        return ResponseEntity.ok(createdOrder)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<OrderResponse> {
        val order = orderService.getOrderById(id)
        return ResponseEntity.ok(order)
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    fun getAllOrders(): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity.ok(orders)
    }

    @PutMapping("/status")
    @Operation(summary = "Update order status")
    fun updateOrderStatus(@Valid @RequestBody statusUpdate: OrderStatusUpdate): ResponseEntity<OrderResponse> {
        val updatedOrder = orderService.updateOrderStatus(statusUpdate)
        return ResponseEntity.ok(updatedOrder)
    }

    // TODO: Move this to user controller (because it is for getting one user's orders)
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user ID")
    fun getOrdersByUserId(@PathVariable userId: Long): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getOrdersByUserId(userId)
        return ResponseEntity.ok(orders)
    }

    // TODO: Move this to product controller (because it is for getting one product's orders)
    @GetMapping("/product/{productId}")
    @Operation(summary = "Get orders by product ID")
    fun getOrdersByProductId(@PathVariable productId: Long): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getOrdersByProductId(productId)
        return ResponseEntity.ok(orders)
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
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<String> {
        val result = orderService.deleteOrder(id)
        return ResponseEntity.ok(result)
    }
}
