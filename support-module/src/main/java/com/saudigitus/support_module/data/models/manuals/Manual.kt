package com.saudigitus.support_module.data.models.manuals

import com.fasterxml.jackson.databind.ObjectMapper
import com.saudigitus.support_module.utils.Mapper.translateJsonToObject

class Manual {
    private fun toJson(): String = translateJsonToObject().writeValueAsString(this)

    companion object {
        fun fromJson(json: String?): List<ManualItem>? = if (json != null) {
            val mapper = ObjectMapper()

            translateJsonToObject()
                .readValue(
                    json,
                    mapper.typeFactory.constructCollectionType(
                        List::class.java,
                        ManualItem::class.java,
                    ),
                )
        } else {
            null
        }
    }
}
