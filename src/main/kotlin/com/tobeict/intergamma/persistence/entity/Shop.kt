package com.tobeict.intergamma.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "shop")
data class Shop(

    @Id
    val id: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "address")
    val address: String
)
