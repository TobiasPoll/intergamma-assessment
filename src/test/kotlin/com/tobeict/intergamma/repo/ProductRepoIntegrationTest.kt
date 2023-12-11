package com.tobeict.intergamma.repo

import com.tobeict.intergamma.IntergammaApplication
import com.tobeict.intergamma.persistence.entity.Product
import com.tobeict.intergamma.persistence.repo.ProductRepo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IntergammaApplication::class])
class ProductRepoIntegrationTest(
    @Autowired val productRepo: ProductRepo
) {

    @BeforeEach
    @AfterEach
    fun clearDb() {
        productRepo.deleteAll()
    }

    @Test
    fun persistenceTest() {
        val product = Product(
            id = UUID.randomUUID().toString(),
            name = "Schroef 2cm",
            description = "Mooie schroef van 2cm lang",
            price = BigDecimal("2.9900")
        )
        productRepo.save(product)
        val allProducts = productRepo.findAll()
        assertAll("product",
            { assertEquals(allProducts.size, 1) },
            { assertEquals(allProducts.first(), product) }
        )
    }
}