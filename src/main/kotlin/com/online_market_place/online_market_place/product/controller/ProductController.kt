package com.online_market_place.online_market_place.product.controller

import com.online_market_place.online_market_place.common.ApiResponse
import com.online_market_place.online_market_place.common.annotation.IsAdminOrSeller
import com.online_market_place.online_market_place.common.annotation.IsAdminOrSellerOrCustomer
import com.online_market_place.online_market_place.product.dto.CreateProductDTO
import com.online_market_place.online_market_place.product.dto.ProductResponse
import com.online_market_place.online_market_place.product.dto.UpdateProductRequest
import com.online_market_place.online_market_place.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/products")
@SecurityRequirement(name = "bearerAuth")
@Suppress("unused") // Suppress unused warning for the class since this is a controller class
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    @Operation(summary = "Create a new product")
    @IsAdminOrSeller
    fun createProduct(@Valid @RequestBody request: CreateProductDTO.Input): ResponseEntity<CreateProductDTO.Output> {
        val createdProduct = productService.createProduct(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
    }

    @GetMapping
    @Operation(summary = "Get all products")
    @IsAdminOrSellerOrCustomer
    fun getAllProducts(): ResponseEntity<List<ProductResponse>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @IsAdminOrSellerOrCustomer
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    @IsAdminOrSeller
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = productService.updateProduct(id, request)
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    @IsAdminOrSeller
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        val apiResponse = ApiResponse(
            message = productService.deleteProduct(id),
           success = true
        )

        return ResponseEntity.ok(apiResponse)
    }
}
