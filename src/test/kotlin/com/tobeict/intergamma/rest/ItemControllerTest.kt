package com.tobeict.intergamma.rest

import com.tobeict.intergamma.IntergammaApplication
import com.tobeict.intergamma.model.ItemDto
import com.tobeict.intergamma.model.ProductDto
import com.tobeict.intergamma.model.ShopDto
import com.tobeict.intergamma.persistence.exception.UnknownEntityException
import com.tobeict.intergamma.service.ItemService
import com.tobeict.intergamma.service.ProductService
import com.tobeict.intergamma.service.ShopService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IntergammaApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest(
    @Autowired val shopService: ShopService,
    @Autowired val productService: ProductService,
    @Autowired val itemService: ItemService
) {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @BeforeEach
    @AfterEach
    fun setup() {
        shopService.deleteAll()
        productService.deleteAll()
        itemService.deleteAll()
    }

    @Test
    fun createItemTest() {
        val shop = storeTestShop()
        val product = storeTestProduct()

        val item = ItemDto(
            id = UUID.randomUUID().toString(),
            shop = shop,
            product = product
        )

        val createdItem = restTemplate!!.postForEntity("http://localhost:${port}/items", item, ItemDto::class.java)
        assertThat(createdItem.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createdItem.body).isEqualTo(item)

        assertThat(itemService.findById(item.id)).isEqualTo(createdItem.body)
    }

    @Test
    fun getItemTest() {
        val shop = storeTestShop()
        val product = storeTestProduct()

        val item = ItemDto(
            id = UUID.randomUUID().toString(),
            shop = shop,
            product = product
        )
        itemService.storeNew(item)

        val gotItem = restTemplate!!.getForEntity("http://localhost:${port}/items/${item.id}", ItemDto::class.java)
        assertThat(gotItem.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(gotItem.body).isEqualTo(item)
    }

    @Test
    fun getAllItemTest() {
        val shop = storeTestShop()
        val product = storeTestProduct()

        val item = ItemDto(
            id = UUID.randomUUID().toString(),
            shop = shop,
            product = product
        )
        itemService.storeNew(item)

        val allItem = restTemplate!!.getForEntity("http://localhost:${port}/items", Array<ItemDto>::class.java)
        assertThat(allItem.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(allItem.body).contains(item)
    }

    @Test
    fun getShopProductsTest() {
        val shop = storeTestShop()
        val product = storeTestProduct()

        val item = ItemDto(
            id = UUID.randomUUID().toString(),
            shop = shop,
            product = product
        )
        itemService.storeNew(item)

        val shopProductItems = restTemplate!!.exchange(
            "http://localhost:${port}/items/shop_products?shopId={shop}&productId={product}",
            HttpMethod.GET,
            HttpEntity<ItemDto>(item, null),
            Array<ItemDto>::class.java,
            shop.id,
            product.id
        )
        assertThat(shopProductItems.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(shopProductItems.body).contains(item)
    }

    @Test
    fun updateItemTest() {
        val shop = storeTestShop()
        val product = storeTestProduct()
        val otherProduct = ProductDto(
            id = UUID.randomUUID().toString(),
            name = "Schroevendraaier",
            description = "Rode schroevendraaier",
            price = BigDecimal("6.9500")
        )
        productService.storeNew(otherProduct)

        val item = ItemDto(
            id = UUID.randomUUID().toString(),
            shop = shop,
            product = product
        )
        itemService.storeNew(item)

        val updatedItemDto = item.copy(product = otherProduct)

        val updatedItemFromController = restTemplate!!.exchange(
            "http://localhost:${port}/items",
            HttpMethod.PUT,
            HttpEntity<ItemDto>(updatedItemDto, null),
            ItemDto::class.java
        )
        assertThat(updatedItemFromController.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(updatedItemFromController.body).isEqualTo(updatedItemDto)
    }

    @Test
    fun deleteItemTest() {
        val shop = storeTestShop()
        val product = storeTestProduct()

        val item = ItemDto(
            id = UUID.randomUUID().toString(),
            shop = shop,
            product = product
        )
        itemService.storeNew(item)

        val updatedItemFromController = restTemplate!!.exchange(
            "http://localhost:${port}/items/{itemId}",
            HttpMethod.DELETE,
            HttpEntity<ItemDto>(item, null),
            ItemDto::class.java,
            item.id
        )
        assertThat(updatedItemFromController.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(updatedItemFromController.body).isEqualTo(item)

        assertThrows<UnknownEntityException> {
            itemService.findById(item.id)
        }
    }


    @Test
    fun reserveItemTest() {
        val shop = storeTestShop()
        val product = storeTestProduct()

        val item = ItemDto(
            id = UUID.randomUUID().toString(),
            shop = shop,
            product = product
        )

        val createdEntity = restTemplate!!.postForEntity("http://localhost:${port}/items", item, ItemDto::class.java)
        assertThat(createdEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createdEntity.body).isEqualTo(item)

        val reservedEntity =
            restTemplate.postForEntity("http://localhost:${port}/items/reserve/${item.id}", item, ItemDto::class.java)
        assertThat(reservedEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(reservedEntity.body?.id).isEqualTo(item.id)
        assertThat(reservedEntity.body?.reserved).isEqualTo(true)

        val deletedEntity = restTemplate.exchange(
            "http://localhost:${port}/items/{item.id}", HttpMethod.DELETE, HttpEntity<ItemDto>(item, null),
            Void::class.java, item.id
        )
        assertThat(deletedEntity.statusCode).isEqualTo(HttpStatus.LOCKED)

        val gotItem = restTemplate.getForEntity("http://localhost:${port}/items/${item.id}", ItemDto::class.java)
        assertThat(gotItem.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(gotItem.body?.id).isEqualTo(item.id)
        assertThat(reservedEntity.body?.reserved).isEqualTo(true)
    }

    private fun storeTestProduct(): ProductDto {
        val product = ProductDto(
            id = UUID.randomUUID().toString(),
            name = "Zaag",
            description = "Scherpe Zaag",
            price = BigDecimal("12.9900")
        )
        productService.storeNew(product)
        return product
    }

    private fun storeTestShop(): ShopDto {
        val shop = ShopDto(
            "shop1",
            name = "ABCD",
            address = "adadad"
        )
        shopService.storeNew(shop)
        return shop
    }
}