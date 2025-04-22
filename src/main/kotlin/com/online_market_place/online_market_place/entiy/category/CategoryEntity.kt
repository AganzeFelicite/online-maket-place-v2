
package com.online_market_place.online_market_place.entiy.category
import com.online_market_place.online_market_place.entiy.product.ProductEntity
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
