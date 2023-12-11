package com.tobeict.intergamma.model

import kotlinx.serialization.Serializable

@Serializable
data class ShopDto(
    val id: String,
    val name: String,
    val address: String
)
