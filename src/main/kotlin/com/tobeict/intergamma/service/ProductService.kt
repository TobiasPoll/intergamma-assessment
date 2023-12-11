package com.tobeict.intergamma.service

import com.tobeict.intergamma.model.ProductDto
import com.tobeict.intergamma.persistence.entity.Product
import com.tobeict.intergamma.persistence.exception.UnknownEntityException
import com.tobeict.intergamma.persistence.repo.ProductRepo
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
@Transactional(Transactional.TxType.REQUIRES_NEW)
class ProductService(
    val productRepo: ProductRepo
) {

    fun storeNew(productDto: ProductDto): ProductDto {
        logger.info { "Storing new product $productDto" }
        val product = productDto.toEntity()
        return productRepo.save(product)
            .toDto()
    }

    fun findAll(): List<ProductDto> {
        logger.info { "Retrieving all products" }
        return productRepo.findAll()
            .map { it.toDto() }
    }

    fun findById(id: String): ProductDto? {
        logger.info { "Retrieving product with id $id" }
        return productRepo.findByIdOrNull(id)
            ?.toDto()
    }

    fun update(productDto: ProductDto): ProductDto {
        logger.info { "Updating product $productDto" }
        val updated = productRepo.findById(productDto.id)
            .orElseThrow { UnknownEntityException("product", productDto.id) }
            .copy(
                name = productDto.name,
                description = productDto.description,
                price = productDto.price
            )
        return productRepo.save(updated)
            .toDto()
    }

    fun delete(productId: String): ProductDto {
        logger.info { "Deleting product $productId" }
        val product = productRepo.findById(productId)
            .orElseThrow { UnknownEntityException("product", productId) }
        productRepo.delete(product)
        return product.toDto()
    }

    fun deleteAll() {
        logger.info { "Deleting all products" }
        productRepo.deleteAll()
    }
}

fun Product.toDto(): ProductDto =
    ProductDto(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price
    )

fun ProductDto.toEntity(): Product =
    Product(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price
    )
