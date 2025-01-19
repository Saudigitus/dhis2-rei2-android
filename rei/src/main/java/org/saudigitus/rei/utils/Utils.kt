package org.saudigitus.rei.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.hisp.dhis.android.core.D2
import org.saudigitus.rei.utils.ObjectMapper.translateJsonToObject

object Utils {
    inline fun <reified T> fromJson(json: String?): T? = if (json != null) {
        translateJsonToObject()
            .readValue(
                json,
                T::class.java,
            )
    } else {
        null
    }

    inline fun <reified T> buildListFromJson(json: String?): List<T>? = if (json != null) {
        val mapper = ObjectMapper()

        translateJsonToObject()
            .readValue(
                json,
                mapper.typeFactory.constructCollectionType(
                    List::class.java,
                    T::class.java,
                ),
            )
    } else {
        null
    }

    fun <T> T.toJson(): String = translateJsonToObject().writeValueAsString(this)

    fun isRei(d2: D2, program: String): Boolean {
        val config = d2.reiModuleDatastore()
            .find { it.program == program }

        return config != null && (config.defaults.displayStages || config.defaults.displaySupport)
    }
}
