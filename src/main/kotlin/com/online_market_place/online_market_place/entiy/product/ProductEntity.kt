package com.online_market_place.online_market_place.entiy.product

import com.online_market_place.online_market_place.entiy.category.CategoryEntity
import jakarta.persistence.*

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,


    var name: String,
    var description: String,
    var price: Double,
    var stock: Int,
    var isFeatured: Boolean = false,
    var productImageUrl: String? = null,



    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: CategoryEntity,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL])
    val reviews: List<ProductReviewEntity> = mutableListOf()
)
