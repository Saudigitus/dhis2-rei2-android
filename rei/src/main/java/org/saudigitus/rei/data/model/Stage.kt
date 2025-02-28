package org.saudigitus.rei.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Stage(
    @JsonProperty("uid")
    val uid: String,
    @JsonProperty("displayName")
    val displayName: String?,
    @JsonProperty("description")
    val description: String?
)
