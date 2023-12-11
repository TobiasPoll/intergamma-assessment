package com.tobeict.intergamma.persistence.repo

import com.tobeict.intergamma.persistence.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ItemRepo : JpaRepository<Item, String> {

    fun findAllByShopIdAndProductId(shopId: String, productId: String): List<Item>

    @Query("UPDATE Item set reservedAt=null where reservedAt < :endInstant")
    @Modifying
    fun clearAllReservedBefore(endInstant: Instant): Int
}