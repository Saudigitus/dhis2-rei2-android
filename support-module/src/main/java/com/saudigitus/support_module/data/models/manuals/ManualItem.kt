package com.saudigitus.support_module.data.models.manuals


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
data class ManualItem(
    @JsonProperty("uid")
    @PrimaryKey val uid: String,

    @JsonProperty("title")
    @ColumnInfo(name = "title")  val title: String,

    @JsonProperty("subtitle")
    @ColumnInfo(name = "subtitle")  val subtitle: String?,

    @JsonProperty("path")
    @ColumnInfo(name = "path") val path: String?
)
