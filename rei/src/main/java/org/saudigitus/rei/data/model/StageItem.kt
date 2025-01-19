package org.saudigitus.rei.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class StageItem(
    @JsonProperty("color")
    val color: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("pos")
    val pos: Int,
)
