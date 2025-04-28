package com.online_market_place.online_market_place.product.service

import com.online_market_place.online_market_place.product.dto.CreateProductRequest
import com.online_market_place.online_market_place.product.dto.ProductResponse
import com.online_market_place.online_market_place.product.dto.UpdateProductRequest

interface ProductService {
    fun createProduct(productCreationRequest: CreateProductRequest): ProductResponse
    fun getAllProducts(): List<ProductResponse>
    fun getProductById(id: Long): ProductResponse
    fun updateProduct(id: Long, productUpdateRequest: UpdateProductRequest): ProductResponse
    fun deleteProduct(id: Long): String

}