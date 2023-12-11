package com.tobeict.intergamma.model

import com.tobeict.intergamma.serializer.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class ProductDto(

    val id: String,
    val name: String,
    val description: String?,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal

)
