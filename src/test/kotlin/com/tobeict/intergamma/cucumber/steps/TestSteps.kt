package com.tobeict.intergamma.cucumber.steps

import com.tobeict.intergamma.model.ItemDto
import com.tobeict.intergamma.model.ProductDto
import com.tobeict.intergamma.model.ShopDto
import com.tobeict.intergamma.service.ItemService
import com.tobeict.intergamma.service.ProductService
import com.tobeict.intergamma.service.ShopService
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ResponseStatus
import java.math.BigDecimal
import java.time.Instant


class TestSteps(
    val shopService: ShopService,
    val productService: ProductService,
    val itemService: ItemService,
    val restTemplate: TestRestTemplate
) {

    @LocalServerPort
    private var port: Int = 0

    private var responseStatus: HttpStatusCode? = null
    private var responseItem: ItemDto? = null


    @Given("all shops are removed from the database")
    fun allShopsAreRemovedFromTheDatabase() {
        shopService.deleteAll()
    }

    @Given("all products are removed from the database")
    fun allProductsAreRemovedFromTheDatabase() {
        productService.deleteAll()
    }

    @Given("all items are removed from the database")
    fun allItemsAreRemovedFromTheDatabase() {
        itemService.deleteAll()
    }

    @Given("I have the following shops:")
    fun iHaveTheFollowingShop(dataTable: DataTable) {
        dataTable.asMaps()
            .map { toShopDto(it) }
            .forEach { shopService.storeNew(it) }

    }

    @And("I have the following products:")
    fun iHaveTheFollowingProducts(dataTable: DataTable) {
        dataTable.asMaps()
            .map { toProductDto(it) }
            .forEach { productService.storeNew(it) }
    }

    @And("I have the following items:")
    fun iHaveTheFollowingItems(dataTable: DataTable) {
        dataTable.asMaps()
            .map { toItemDto(it) }
            .forEach { itemService.storeNew(it) }
    }

    @When("I make a post request to endpoint {string} with item:")
    fun iMakeAPostRequestToEndpointWithItem(urlSuffix: String, dataTable: DataTable) {
        val itemDto = dataTable.asMaps()
            .map { toItemDto(it) }
            .first()
        val response = restTemplate.postForEntity("http://localhost:${port}/${urlSuffix}", itemDto, ItemDto::class.java)
        this.responseStatus = response.statusCode
        this.responseItem = response.body
    }

    @When("I make a post request to endpoint {string}")
    fun iMakeAPostRequestToEndpoint(urlSuffix: String) {
        val response = restTemplate.postForEntity("http://localhost:${port}/${urlSuffix}", null, ItemDto::class.java)
        this.responseStatus = response.statusCode
        this.responseItem = response.body
    }

    @When("I make a delete request to endpoint {string}")
    fun iMakeADeleteRequestToEndpoint(urlSuffix: String) {
        val response = restTemplate.exchange(
            "http://localhost:${port}/${urlSuffix}", HttpMethod.DELETE, HttpEntity<ItemDto>(null, null),
            Void::class.java
        )
        this.responseStatus = response.statusCode
    }

    @Then("the response has status code {int}")
    fun theResponseHasStatusCode(statusCode: Int) {
        assertThat(responseStatus?.value()).isEqualTo(statusCode)
    }

    @When("the reservation scheduler has run {int} minutes later")
    fun theReservationSchedulerHasRunMinutesLater(minutes: Int) {
        itemService.clearReservationUntil(Instant.now())
    }

    @And("the response has the item:")
    fun theResponseHasTheItem(dataTable: DataTable) {
        val itemDto = dataTable.asMaps()
            .map { toItemDto(it) }
            .first()
        assertThat(responseItem).isEqualTo(itemDto)
    }

    private fun toItemDto(itemMap: Map<String, String>): ItemDto =
        ItemDto(
            id = itemMap["id"]!!,
            product = productService.findById(itemMap["product_id"]!!)!!,
            shop = shopService.findById(itemMap["shop_id"]!!)!!,
            reserved = itemMap["reserved"]?.toBoolean() ?: false
        )

    private fun toShopDto(shopMap: Map<String, String>): ShopDto =
        ShopDto(
            id = shopMap["id"]!!,
            name = shopMap["name"]!!,
            address = shopMap["address"]!!,
        )

    private fun toProductDto(productMap: Map<String, String>): ProductDto =
        ProductDto(
            id = productMap["id"]!!,
            name = productMap["name"]!!,
            description = productMap["description"]!!,
            price = BigDecimal(productMap["price"]!!)
        )
}