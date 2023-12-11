package com.tobeict.intergamma.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "product")
data class Product(

    @Id
    val id: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "description")
    val description: String?,

    @Column(name = "price")
    val price: BigDecimal
)
