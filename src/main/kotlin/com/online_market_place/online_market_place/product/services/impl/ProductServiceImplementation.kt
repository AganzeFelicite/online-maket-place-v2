package com.online_market_place.online_market_place.product.services.impl

import com.online_market_place.online_market_place.category.repositories.CategoryRepository
import com.online_market_place.online_market_place.common.exceptions.ResourceNotFoundException
import com.online_market_place.online_market_place.product.dto.CreateProductDTO
import com.online_market_place.online_market_place.product.dto.UpdateProductDTO
import com.online_market_place.online_market_place.product.entities.ProductEntity
import com.online_market_place.online_market_place.product.mappers.toProductEntity
import com.online_market_place.online_market_place.product.mappers.toProductResponse
import com.online_market_place.online_market_place.product.repositories.ProductRepository
import com.online_market_place.online_market_place.product.services.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ProductServiceImplementation(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
): ProductService {

    /**
     * method to create a product
     * this method receives a product creation request and returns a product response
     * @param productCreationRequest
     * @return ProductResponse
     */
    override fun createProduct(productCreationRequest: CreateProductDTO.Input): CreateProductDTO.Output {
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

    override fun getValidatedProducts(productIds: List<Long>): Map<Long, ProductEntity> {
        val products = productRepository.findAllById(productIds).associateBy { it.id }
        val missingIds = productIds.filterNot { products.containsKey(it) }

        if (missingIds.isNotEmpty()) {
            throw ResourceNotFoundException("Products not found: $missingIds")
        }

        return products
    }

    override fun getAllProducts(): List<CreateProductDTO.Output> {
        val products = productRepository.findAll()



        return products.map { it.toProductResponse() }
    }

    override fun getProductById(id: Long): CreateProductDTO.Output {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product with $id is not Found") }

        return product.toProductResponse()
    }

    override fun updateProduct(id: Long, productUpdateRequest: UpdateProductDTO.Input): CreateProductDTO.Output {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product with ID $id not found") }

        val category = categoryRepository.findById(productUpdateRequest.categoryId)
            .orElseThrow { ResourceNotFoundException("Category with ID ${productUpdateRequest.categoryId} not found") }

        product.updateProductDetails(
            name = productUpdateRequest.name,
            description = productUpdateRequest.description,
            price = productUpdateRequest.price,
            stock = productUpdateRequest.stockQuantity,
            isFeatured = productUpdateRequest.featured,
            productImageUrl = productUpdateRequest.imageUrl,
            category = category
        )

        return productRepository.save(product).toProductResponse()
    }


    @Transactional
    override fun updateProductStock(productId: Long, quantityToDeduct: Int) {
        val product = productRepository.findById(productId)
            .orElseThrow { ResourceNotFoundException("Product with ID $productId not found") }

        if (product.stock < quantityToDeduct) {
            throw IllegalStateException("Insufficient stock for product ${product.name}")
        }

        product.updateStock(quantityToDeduct)
        productRepository.save(product)
    }

    override fun deleteProduct(id: Long): String {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product with ID $id not found") }

        productRepository.delete(product)

        return "Product with ID $id deleted successfully"
    }
}