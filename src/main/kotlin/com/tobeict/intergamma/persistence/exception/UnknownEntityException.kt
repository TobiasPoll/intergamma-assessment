package com.tobeict.intergamma.persistence.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Unknown entity")
class UnknownEntityException(
    val entity: String,
    val id: String
) : RuntimeException("No $entity known for id $id")