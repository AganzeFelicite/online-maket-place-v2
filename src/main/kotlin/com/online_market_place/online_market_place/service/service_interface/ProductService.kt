package com.online_market_place.online_market_place.service.service_interface

import com.online_market_place.online_market_place.dto.product.CreateProductRequest
import com.online_market_place.online_market_place.dto.product.ProductResponse
import com.online_market_place.online_market_place.dto.product.UpdateProductRequest

interface ProductService {
    fun createProduct(productCreationRequest: CreateProductRequest): ProductResponse
    fun getAllProducts(): List<ProductResponse>
    fun getProductById(id: Long): ProductResponse
    fun updateProduct(id: Long, productUpdateRequest: UpdateProductRequest): ProductResponse
    fun deleteProduct(id: Long): String

}