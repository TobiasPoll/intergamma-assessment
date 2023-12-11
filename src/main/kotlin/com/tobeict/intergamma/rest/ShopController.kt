package com.tobeict.intergamma.rest

import com.tobeict.intergamma.model.ShopDto
import com.tobeict.intergamma.service.ShopService
import io.swagger.v3.oas.annotations.Operation
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
class ShopController(
    val shopService: ShopService
) {

    @Operation(summary = "Create a new Shop")
    @PostMapping(
        "/shops",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createItem(@RequestBody shopDto: ShopDto): ShopDto {
        logger.info { "Received request to create shop $shopDto" }
        return shopService.storeNew(shopDto)
    }

    @Operation(summary = "Retrieve a Shop by its Id")
    @GetMapping(
        "/shops/{shopId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findItem(@PathVariable shopId: String): ShopDto? {
        logger.info { "Received request to retrieve shop $shopId" }
        return shopService.findById(shopId)
    }

    @Operation(summary = "Retrieve all Shops")
    @GetMapping(
        "/shops",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findAll(): List<ShopDto> {
        logger.info { "Received request to retrieve all shops" }
        return shopService.findAll()
    }


    @Operation(summary = "Update a Shop")
    @PutMapping(
        "/shops",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateItem(@RequestBody shopDto: ShopDto): ShopDto {
        logger.info { "Received request to update shop $shopDto" }
        return shopService.update(shopDto)
    }

    @Operation(summary = "Delete a Shop")
    @DeleteMapping(
        "/shops/{shopId}"
    )
    fun deleteItem(@PathVariable shopId: String): ShopDto {
        logger.info { "Received request to delete shop $shopId" }
        return shopService.delete(shopId)
    }

}