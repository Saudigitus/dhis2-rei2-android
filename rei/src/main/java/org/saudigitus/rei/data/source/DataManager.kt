package org.saudigitus.rei.data.source

import androidx.compose.ui.graphics.Color
import org.saudigitus.rei.data.model.AppConfigItem
import org.saudigitus.rei.data.model.SearchTeiModel
import org.saudigitus.rei.data.model.Stage

interface DataManager {
    suspend fun loadConfig(): List<AppConfigItem>

    suspend fun getStages(
        program: String,
    ): List<Stage>

    suspend fun getTeis(
        program: String,
        stage: String?,
        eventDate: String?
    ): List<SearchTeiModel>

    suspend fun getStageEventData(
        program: String,
        stage: String,
    ): List<Triple<String, String, Color>>
}
