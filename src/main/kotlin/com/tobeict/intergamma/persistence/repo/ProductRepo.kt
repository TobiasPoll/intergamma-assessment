package com.tobeict.intergamma.persistence.repo

import com.tobeict.intergamma.persistence.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepo : JpaRepository<Product, String> {
}