package org.saudigitus.rei.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LineListing(
    @JsonProperty("ccvDataElement")
    val ccvDataElement: String,
    @JsonProperty("stageVaccination")
    val stageVaccination: String,
    @JsonProperty("status")
    val status: List<Status>,
)
