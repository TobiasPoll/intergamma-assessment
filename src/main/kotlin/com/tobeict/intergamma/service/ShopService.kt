package com.tobeict.intergamma.service

import com.tobeict.intergamma.model.ShopDto
import com.tobeict.intergamma.persistence.entity.Shop
import com.tobeict.intergamma.persistence.exception.UnknownEntityException
import com.tobeict.intergamma.persistence.repo.ShopRepo
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
@Transactional(Transactional.TxType.REQUIRES_NEW)
class ShopService(
    val shopRepo: ShopRepo
) {

    fun storeNew(shopDto: ShopDto): ShopDto {
        logger.info { "Storing new shop $shopDto" }
        val shop = shopDto.toEntity()
        return shopRepo.save(shop)
            .toDto()
    }

    fun findAll(): List<ShopDto> {
        logger.info { "Retrieving all shops" }
        return shopRepo.findAll()
            .map { it.toDto() }
    }

    fun findById(id: String): ShopDto? {
        logger.info { "Retrieving shop with id $id" }
        return shopRepo.findByIdOrNull(id)
            ?.toDto()
    }

    fun update(shopDto: ShopDto): ShopDto {
        logger.info { "Updating shop $shopDto" }
        val updatedShop = shopRepo.findById(shopDto.id)
            .orElseThrow { UnknownEntityException("shop", shopDto.id) }
            .copy(
                name = shopDto.name,
                address = shopDto.address
            )
        return shopRepo.save(updatedShop)
            .toDto()
    }

    fun delete(shopId: String): ShopDto {
        logger.info { "Deleting shop $shopId" }
        val shop = shopRepo.findById(shopId)
            .orElseThrow { UnknownEntityException("shop", shopId) }
        shopRepo.delete(shop)
        return shop.toDto()
    }

    fun deleteAll() {
        logger.info { "Deleting all shops" }
        shopRepo.deleteAll()
    }

}

fun Shop.toDto() = ShopDto(
    id = this.id,
    name = this.name,
    address = this.address
)


fun ShopDto.toEntity(): Shop = Shop(
    id = this.id,
    name = this.name,
    address = this.address
)

