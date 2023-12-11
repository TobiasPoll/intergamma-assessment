package com.tobeict.intergamma.persistence.repo

import com.tobeict.intergamma.persistence.entity.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShopRepo : JpaRepository<Shop, String> {
}