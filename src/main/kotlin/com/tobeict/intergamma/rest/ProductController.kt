package com.tobeict.intergamma.rest

import com.tobeict.intergamma.model.ProductDto
import com.tobeict.intergamma.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
class ProductController(
    val productService: ProductService
) {

    @Operation(summary = "Create a Product")
    @PostMapping(
        "/products",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createItem(@RequestBody productDto: ProductDto): ProductDto {
        logger.info { "Received request to create product $productDto" }
        return productService.storeNew(productDto)
    }

    @Operation(summary = "Retrieve a Product by its Id")
    @GetMapping(
        "/products/{productId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findItem(@PathVariable productId: String): ProductDto? {
        logger.info { "Received request to retrieve product $productId" }
        return productService.findById(productId)
    }

    @Operation(summary = "Retrieve all Products")
    @GetMapping(
        "/products",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findAll(): List<ProductDto> {
        logger.info { "Received request to retrieve all products" }
        return productService.findAll()
    }

    @Operation(summary = "Update a Product")
    @PutMapping(
        "/products",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateItem(@RequestBody productDto: ProductDto): ProductDto {
        logger.info { "Received request to update product $productDto" }
        return productService.update(productDto)
    }

    @Operation(summary = "Delete a Product")
    @DeleteMapping(
        "/products/{productId}"
    )
    fun deleteItem(@PathVariable productId: String): ProductDto {
        logger.info { "Received request to delete product $productId" }
        return productService.delete(productId)
    }

}