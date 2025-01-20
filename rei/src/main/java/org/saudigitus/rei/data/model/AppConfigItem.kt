package org.saudigitus.rei.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppConfigItem(
    @JsonProperty("defaults")
    val defaults: Defaults,
    @JsonProperty("lineListing")
    val lineListing: LineListing,
    @JsonProperty("program")
    val program: String,
    @JsonProperty("stageItems")
    val stageItems: List<StageItem>,
)
