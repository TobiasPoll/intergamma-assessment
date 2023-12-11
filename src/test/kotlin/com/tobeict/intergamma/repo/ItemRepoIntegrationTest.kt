package com.tobeict.intergamma.repo

import com.tobeict.intergamma.IntergammaApplication
import com.tobeict.intergamma.persistence.entity.Item
import com.tobeict.intergamma.persistence.entity.Product
import com.tobeict.intergamma.persistence.entity.Shop
import com.tobeict.intergamma.persistence.repo.ItemRepo
import com.tobeict.intergamma.persistence.repo.ProductRepo
import com.tobeict.intergamma.persistence.repo.ShopRepo
import org.junit.After
import org.junit.Before
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
class ItemRepoIntegrationTest(
    @Autowired val productRepo: ProductRepo,
    @Autowired val shopRepo: ShopRepo,
    @Autowired val itemRepo: ItemRepo
) {

    @BeforeEach
    @AfterEach
    fun clearDb() {
        productRepo.deleteAll()
        shopRepo.deleteAll()
        itemRepo.deleteAll()
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

        val shop = Shop(
            id = UUID.randomUUID().toString(),
            name = "GAMMA bouwmarkt Nieuw Overvecht",
            address = "Nebraskadreef 18, 3565 AG Utrecht"
        )
        shopRepo.save(shop)

        val item = Item(
            id = UUID.randomUUID().toString(),
            product = product,
            shop = shop
        )
        itemRepo.save(item)

        val allProductItems = itemRepo.findAll()
        assertAll("product",
            { assertEquals(allProductItems.size, 1) },
            { assertEquals(allProductItems.first(), item) },
            { assertEquals(allProductItems.first().product, product) },
            { assertEquals(allProductItems.first().shop, shop) }
        )
    }

    @Test
    fun findByShopAndProduct() {
        val product = Product(
            id = UUID.randomUUID().toString(),
            name = "Schroef 2cm",
            description = "Mooie schroef van 2cm lang",
            price = BigDecimal("2.9900")
        )
        productRepo.save(product)

        val shop = Shop(
            id = UUID.randomUUID().toString(),
            name = "GAMMA bouwmarkt Nieuw Overvecht",
            address = "Nebraskadreef 18, 3565 AG Utrecht"
        )
        shopRepo.save(shop)

        val item = Item(
            id = UUID.randomUUID().toString(),
            product = product,
            shop = shop
        )
        itemRepo.save(item)

        val allProductItems = itemRepo.findAllByShopIdAndProductId(shop.id, product.id)
        assertAll("product",
            { assertEquals(allProductItems.size, 1) },
            { assertEquals(allProductItems.first(), item) },
            { assertEquals(allProductItems.first().product, product) },
            { assertEquals(allProductItems.first().shop, shop) }
        )
    }
}