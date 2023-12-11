package com.tobeict.intergamma.rest

import com.tobeict.intergamma.model.ItemDto
import com.tobeict.intergamma.service.ItemService
import io.swagger.v3.oas.annotations.Operation
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}
@RestController
class ItemController(
    val itemService: ItemService
) {

    @Operation(summary = "Create an Item")
    @PostMapping(
        "/items",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createItem(@RequestBody itemDto: ItemDto): ItemDto {
        logger.info { "Received request to create item $itemDto" }
        return itemService.storeNew(itemDto)
    }

    @Operation(summary = "Retrieve an Item by its Id")
    @GetMapping(
        "/items/{itemId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findItem(@PathVariable itemId: String): ItemDto? {
        logger.info { "Received request to retrieve item $itemId" }
        return itemService.findById(itemId)
    }

    @Operation(summary = "Retrieve all Items")
    @GetMapping(
        "/items",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findAll(): List<ItemDto> {
        logger.info { "Received request to retrieve all items" }
        return itemService.findAll()
    }

    @Operation(summary = "Retrieve all items for a shop and a product")
    @GetMapping(
        "/items/shop_products",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findShopProducts(
        @RequestParam shopId: String,
        @RequestParam productId: String
    ): List<ItemDto> {
        logger.info { "Received request to retrieve all items for shop $shopId and product $productId" }
        return itemService.findShopProducts(shopId, productId)
    }

    @Operation(summary = "Update an Item")
    @PutMapping(
        "/items",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateItem(@RequestBody itemDto: ItemDto): ItemDto {
        logger.info { "Received request to update item $itemDto" }
        return itemService.updateItem(itemDto)
    }

    @Operation(summary = "Delete an Item")
    @DeleteMapping(
        "/items/{itemId}"
    )
    fun deleteItem(@PathVariable itemId: String): ItemDto {
        logger.info { "Received request to delete item $itemId" }
        return itemService.deleteItem(itemId)
    }

    @Operation(summary = "Reserve an Item")
    @PostMapping(
        "/items/reserve/{itemId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun reserveItem(@PathVariable itemId: String): ItemDto {
        logger.info { "Received request to reserve item $itemId" }
        return itemService.reserveItem(itemId)
    }
}