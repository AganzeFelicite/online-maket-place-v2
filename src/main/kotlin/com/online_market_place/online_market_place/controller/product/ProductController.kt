package com.online_market_place.online_market_place.controller

import com.online_market_place.online_market_place.dto.product.CreateProductRequest
import com.online_market_place.online_market_place.dto.product.ProductResponse
import com.online_market_place.online_market_place.dto.product.UpdateProductRequest
import com.online_market_place.online_market_place.service.service_interface.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/products")
@SecurityRequirement(name = "bearerAuth")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    @Operation(summary = "Create a new product")
    fun createProduct(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<ProductResponse> {
        val createdProduct = productService.createProduct(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
    }

    @GetMapping
    @Operation(summary = "Get all products")
    fun getAllProducts(): ResponseEntity<List<ProductResponse>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = productService.updateProduct(id, request)
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<String> {
        val message = productService.deleteProduct(id)
        return ResponseEntity.ok(message)
    }
}
