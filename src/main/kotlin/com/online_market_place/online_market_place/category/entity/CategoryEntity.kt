package com.online_market_place.online_market_place.category.entity

import com.online_market_place.online_market_place.product.entity.ProductEntity
import jakarta.persistence.*

@Entity
@Table(name = "categories")

data class CategoryEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    val products: List<ProductEntity> = mutableListOf()
)
