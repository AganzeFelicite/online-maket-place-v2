package com.online_market_place.online_market_place.product.repositories

import com.online_market_place.online_market_place.product.entities.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


// TODO Always check the warnings
@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
    fun findByCategoryId(categoryId: Long): List<ProductEntity>


}