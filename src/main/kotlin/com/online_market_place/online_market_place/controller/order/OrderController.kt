package com.online_market_place.online_market_place.controller.order
import com.online_market_place.online_market_place.dto.order.OrderCreateRequest
import com.online_market_place.online_market_place.dto.order.OrderResponse
import com.online_market_place.online_market_place.dto.order.OrderStatusUpdate
import com.online_market_place.online_market_place.dto.order.OrderUpdateRequest
import com.online_market_place.online_market_place.entiy.enum_.ProductOrderStatus
import com.online_market_place.online_market_place.service.service_interface.OrderService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(@Valid @RequestBody order: OrderCreateRequest): ResponseEntity<OrderResponse> {
        val createdOrder = orderService.createOrder(order)
        return ResponseEntity.ok(createdOrder)
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<OrderResponse> {
        val order = orderService.getOrderById(id)
        return ResponseEntity.ok(order)
    }

    @GetMapping
    fun getAllOrders(): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity.ok(orders)
    }

    @PutMapping("/status")
    fun updateOrderStatus(@Valid @RequestBody statusUpdate: OrderStatusUpdate): ResponseEntity<OrderResponse> {
        val updatedOrder = orderService.updateOrderStatus(statusUpdate)
        return ResponseEntity.ok(updatedOrder)
    }

    @GetMapping("/user/{userId}")
    fun getOrdersByUserId(@PathVariable userId: Long): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getOrdersByUserId(userId)
        return ResponseEntity.ok(orders)
    }

    @GetMapping("/product/{productId}")
    fun getOrdersByProductId(@PathVariable productId: Long): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getOrdersByProductId(productId)
        return ResponseEntity.ok(orders)
    }

    @GetMapping("/status/{status}")
    fun getOrdersByStatus(@Valid @PathVariable status: ProductOrderStatus): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getOrdersByStatus(status.name)
        return ResponseEntity.ok(orders)
    }

    @PutMapping
    fun updateOrder(@Valid @RequestBody updateRequest: OrderUpdateRequest): ResponseEntity<OrderResponse> {
        val updatedOrder = orderService.updateOrder(updateRequest)
        return ResponseEntity.ok(updatedOrder)
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<String> {
        val result = orderService.deleteOrder(id)
        return ResponseEntity.ok(result)
    }
}
