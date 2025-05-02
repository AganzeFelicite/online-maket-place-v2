package com.online_market_place.online_market_place.review.entities

import com.online_market_place.online_market_place.common.base.BaseEntity
import com.online_market_place.online_market_place.product.entities.ProductEntity
import com.online_market_place.online_market_place.user.entities.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "reviews")
@SQLDelete(sql = "UPDATE reviews SET deleted_at = now() WHERE id=?")
@SQLRestriction("deleted_at is null")
data class ReviewEntity(
    var rating: Int,

    var comment: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: ProductEntity

) : BaseEntity() {
    fun updateReviewDetails(
        rating: Int? = null,
        comment: String? = null
    ) {
        rating?.let { this.rating = it }
        comment?.let { this.comment = it }
    }
}
