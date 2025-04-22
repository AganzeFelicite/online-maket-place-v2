package com.online_market_place.online_market_place.controller

import com.online_market_place.online_market_place.dto.product.CreateProductRequest
import com.online_market_place.online_market_place.dto.product.ProductResponse
import com.online_market_place.online_market_place.dto.product.UpdateProductRequest
import com.online_market_place.online_market_place.service.service_interface.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    fun createProduct(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<ProductResponse> {
        val createdProduct = productService.createProduct(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
    }

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductResponse>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product)
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = productService.updateProduct(id, request)
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<String> {
        val message = productService.deleteProduct(id)
        return ResponseEntity.ok(message)
    }
}
