package com.tobeict.intergamma.persistence.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "item")
data class Item(

    @Id
    val id: String,

    @ManyToOne
    @JoinColumn(name = "shop_id")
    val shop: Shop,

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: Product,

    @Column(name = "reserved_at")
    val reservedAt: Instant? = null
)
