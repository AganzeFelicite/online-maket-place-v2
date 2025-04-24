package com.online_market_place.online_market_place.product.service.impl

import com.online_market_place.online_market_place.category.dto.ProductCategoryRepository
import com.online_market_place.online_market_place.common.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.product.dto.CreateProductRequest
import com.online_market_place.online_market_place.product.dto.ProductResponse
import com.online_market_place.online_market_place.product.dto.UpdateProductRequest
import com.online_market_place.online_market_place.product.repository.ProductRepository
import com.online_market_place.online_market_place.product.service.ProductService
import com.online_market_place.online_market_place.product.toProductEntity
import com.online_market_place.online_market_place.product.toProductResponse
import org.springframework.stereotype.Service


@Service
class ProductServiceImplementation(
    private val productRepository: ProductRepository,
    private val categoryRepository: ProductCategoryRepository
): ProductService {

    /**
     * method to create a product
     * this method receives a product creation request and returns a product response
     * @param productCreationRequest
     * @return ProductResponse
     */
    override fun createProduct(productCreationRequest: CreateProductRequest): ProductResponse {
        val category = categoryRepository.findById(productCreationRequest.categoryId)
            .orElseThrow { ResourceNotFoundException("Category with ID ${productCreationRequest.categoryId} not found") }

        return try {
            val product = productCreationRequest.toProductEntity(category)
            val savedProduct = productRepository.save(product)
            savedProduct.toProductResponse()
        } catch (ex: Exception) {
            throw RuntimeException("Failed to create product: ${ex.message}", ex)
        }
    }

    override fun getAllProducts(): List<ProductResponse> {
        val products = productRepository.findAll()

        if (products.isEmpty()) {
            throw ResourceNotFoundException("No products found")
        }

        return products.map { it.toProductResponse() }
    }

    override fun getProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product with $id is not Found") }

        return product.toProductResponse()
    }

    override fun updateProduct(id: Long, productUpdateRequest: UpdateProductRequest): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product with ID $id not found") }

        val category = categoryRepository.findById(productUpdateRequest.categoryId)
            .orElseThrow { ResourceNotFoundException("Category with ID ${productUpdateRequest.categoryId} not found") }

        product.apply {
            name = productUpdateRequest.name ?: name
            description = productUpdateRequest.description ?: description
            price = productUpdateRequest.price ?: price
            stock = productUpdateRequest.stockQuantity ?: stock
            isFeatured = productUpdateRequest.featured ?: isFeatured
            productImageUrl = productUpdateRequest.imageUrl ?: productImageUrl
            this.category = category ?: this.category
        }

        return productRepository.save(product).toProductResponse()
    }

    override fun deleteProduct(id: Long): String {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product with ID $id not found") }

        productRepository.delete(product)

        return "Product with ID $id deleted successfully"
    }
}