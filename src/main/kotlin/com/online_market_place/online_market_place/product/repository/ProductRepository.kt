package com.online_market_place.online_market_place.product.repository

import com.online_market_place.online_market_place.product.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


// TODO Always check the warnings
@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
    fun findByCategoryId(categoryId: Long): List<ProductEntity>


}