package com.tobeict.intergamma.persistence.exception

import com.tobeict.intergamma.persistence.entity.Item
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.LOCKED, reason = "Item is reserved")
class ReservedItemException(
    val item: Item
) : RuntimeException("Item ${item.id} has been reserved since ${item.reservedAt}")