package com.online_market_place.online_market_place.review.repository

import com.online_market_place.online_market_place.review.entity.ReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<ReviewEntity, Long>