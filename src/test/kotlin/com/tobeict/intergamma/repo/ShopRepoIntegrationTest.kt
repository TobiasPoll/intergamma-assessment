package com.tobeict.intergamma.repo

import com.tobeict.intergamma.IntergammaApplication
import com.tobeict.intergamma.persistence.entity.Shop
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
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IntergammaApplication::class])
class ShopRepoIntegrationTest(
    @Autowired val shopRepo: ShopRepo
) {

    @BeforeEach
    @AfterEach
    fun clearDb() {
        shopRepo.deleteAll()
    }

    @Test
    fun persistenceTest() {
        val shop = Shop(
            id = UUID.randomUUID().toString(),
            name = "GAMMA bouwmarkt Nieuw Overvecht",
            address = "Nebraskadreef 18, 3565 AG Utrecht"
        )
        shopRepo.save(shop)
        val all = shopRepo.findAll()
        assertAll("shop",
            { assertEquals(all.size, 1) },
            { assertEquals(all.first(), shop) }
        )
    }
}