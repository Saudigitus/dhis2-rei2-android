package org.saudigitus.rei.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Status(
    @JsonProperty("color")
    val color: String,
    @JsonProperty("label")
    val label: String,
)
