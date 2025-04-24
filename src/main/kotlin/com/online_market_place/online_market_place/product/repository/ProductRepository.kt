package com.online_market_place.online_market_place.product.repository

import com.online_market_place.online_market_place.category.entity.CategoryEntity
import com.online_market_place.online_market_place.product.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
    // TODO Feedback: Change the parameter to just the categoryId
    fun findAllByCategory(category: CategoryEntity?): List<ProductEntity>
}