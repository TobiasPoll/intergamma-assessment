package com.tobeict.intergamma.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(

    val id: String,
    val product: ProductDto,
    val shop: ShopDto,
    val reserved: Boolean = false

)
