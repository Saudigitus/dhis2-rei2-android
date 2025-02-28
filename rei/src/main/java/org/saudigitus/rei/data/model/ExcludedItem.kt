package org.saudigitus.rei.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExcludedItem(
    @JsonProperty("key")
    val key: String,
    @JsonProperty("stage")
    val stage: String
)
