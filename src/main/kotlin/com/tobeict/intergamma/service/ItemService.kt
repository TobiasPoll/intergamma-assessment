package com.tobeict.intergamma.service

import com.tobeict.intergamma.model.ItemDto
import com.tobeict.intergamma.persistence.entity.Item
import com.tobeict.intergamma.persistence.exception.ReservedItemException
import com.tobeict.intergamma.persistence.exception.UnknownEntityException
import com.tobeict.intergamma.persistence.repo.ItemRepo
import com.tobeict.intergamma.persistence.repo.ProductRepo
import com.tobeict.intergamma.persistence.repo.ShopRepo
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Instant

private val logger = KotlinLogging.logger {}

@Service
@Transactional(Transactional.TxType.REQUIRES_NEW)
class ItemService(
    val productRepo: ProductRepo,
    val shopRepo: ShopRepo,
    val itemRepo: ItemRepo
) {

    fun storeNew(itemDto: ItemDto): ItemDto {
        logger.info { "Storing new Item $itemDto" }
        val shop = shopRepo.findById(itemDto.shop.id)
            .orElseThrow { UnknownEntityException("shop", itemDto.shop.id) }
        val product = productRepo.findById(itemDto.product.id)
            .orElseThrow { UnknownEntityException("product", itemDto.product.id) }

        val item = Item(
            id = itemDto.id,
            product = product,
            shop = shop
        )
        return itemRepo.save(item)
            .toDto()
    }

    fun findAll(): List<ItemDto> {
        logger.info { "Retrieving all items" }
        return itemRepo.findAll()
            .map { it.toDto() }
    }

    fun findShopProducts(shopId: String, productId: String): List<ItemDto> {
       logger.info { "Retrieving all items for shop $shopId and product $productId" }
        return itemRepo.findAllByShopIdAndProductId(shopId, productId)
            .map { it.toDto() }
    }

    fun findById(itemId: String): ItemDto {
        logger.info { "Retrieving items with id $itemId" }
        return itemRepo.findById(itemId)
            .orElseThrow { UnknownEntityException("item", itemId) }
            .toDto()

    }

    fun updateItem(itemDto: ItemDto): ItemDto {
        logger.info { "Updating item $itemDto" }
        val itemDb = retrieveItem(itemDto.id)
        val shop = shopRepo.findById(itemDto.shop.id)
            .orElseThrow { UnknownEntityException("shop", itemDto.shop.id) }
        val product = productRepo.findById(itemDto.product.id)
            .orElseThrow { UnknownEntityException("product", itemDto.product.id) }
        val updated = itemDb.copy(
            shop = shop,
            product = product
        )
        return itemRepo.save(updated)
            .toDto()
    }

    fun deleteItem(itemId: String): ItemDto {
        logger.info { "Deleting item $itemId" }
        val item = retrieveItem(itemId)
        itemRepo.delete(item)
        return item.toDto()
    }

    fun reserveItem(itemId: String): ItemDto {
        logger.info { "Reserving item $itemId" }
        val item = retrieveItem(itemId)
        val updated = item.copy(reservedAt = Instant.now())
        return itemRepo.save(updated)
            .toDto()
    }

    fun clearReservationUntil(reservedAt: Instant): Int {
        logger.info { "Clearing all reservations until $reservedAt" }
        return itemRepo.clearAllReservedBefore(reservedAt)
    }

    private fun retrieveItem(itemId: String): Item {
        val item = itemRepo.findById(itemId)
            .orElseThrow { UnknownEntityException("item", itemId) }
        if (item.reservedAt != null) {
            throw ReservedItemException(item)
        }
        return item
    }

    fun deleteAll() {
        itemRepo.deleteAll()
    }
}

private fun Item.toDto(): ItemDto =
    ItemDto(
        id = this.id,
        product = this.product.toDto(),
        shop = this.shop.toDto(),
        reserved = this.reservedAt != null
    )
