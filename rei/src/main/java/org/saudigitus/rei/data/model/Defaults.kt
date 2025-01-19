package org.saudigitus.rei.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Defaults(
    @JsonProperty("displayStages")
    val displayStages: Boolean,
    @JsonProperty("displaySupport")
    val displaySupport: Boolean,
)
