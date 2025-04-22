package com.online_market_place.online_market_place.repository.product

import com.online_market_place.online_market_place.entiy.category.CategoryEntity
import com.online_market_place.online_market_place.entiy.product.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
    abstract fun findAllByCategory(category: CategoryEntity?): List<ProductEntity>?
}