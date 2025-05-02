package com.online_market_place.online_market_place.review.repositories

import com.online_market_place.online_market_place.review.entities.ReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<ReviewEntity, Long>