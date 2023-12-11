package com.tobeict.intergamma.cucumber.types

import com.fasterxml.jackson.databind.ObjectMapper
import io.cucumber.java.DefaultDataTableCellTransformer
import io.cucumber.java.DefaultDataTableEntryTransformer
import io.cucumber.java.DefaultParameterTransformer
import java.lang.reflect.Type

class DataTableTypes {

    private val objectMapper = ObjectMapper()

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    fun transformer(fromValue: Any?, toValueType: Type?): Any {
        return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType))
    }
}