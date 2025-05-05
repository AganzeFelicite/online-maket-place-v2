package com.online_market_place.online_market_place.product.services

import com.online_market_place.online_market_place.product.dto.CreateProductDTO
import com.online_market_place.online_market_place.product.dto.UpdateProductDTO
import com.online_market_place.online_market_place.product.entities.ProductEntity

interface ProductService {
    fun createProduct(productCreationRequest: CreateProductDTO.Input): CreateProductDTO.Output
    fun getAllProducts(): List<CreateProductDTO.Output>
    fun getProductById(id: Long): CreateProductDTO.Output
    fun updateProduct(id: Long, productUpdateRequest: UpdateProductDTO.Input): CreateProductDTO.Output
    fun deleteProduct(id: Long): String

    fun updateProductStock(productId: Long, quantityToDeduct: Int)

    fun getValidatedProducts(productIds: List<Long>): Map<Long, ProductEntity>

}