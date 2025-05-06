package com.online_market_place.online_market_place.common.base

import jakarta.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    var deletedAt: LocalDateTime? = null
        protected set

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
