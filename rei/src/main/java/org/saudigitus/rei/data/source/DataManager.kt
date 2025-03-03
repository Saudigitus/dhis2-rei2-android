package org.saudigitus.rei.data.source

import androidx.compose.ui.graphics.Color
import org.hisp.dhis.android.core.event.EventStatus
import org.saudigitus.rei.data.model.AppConfigItem
import org.saudigitus.rei.data.model.ExcludedItem
import org.saudigitus.rei.data.model.Quadruple
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
        eventStatus: EventStatus = EventStatus.SCHEDULE
    ): List<SearchTeiModel>

    suspend fun getStageEventData(
        program: String,
        stage: String,
        excludedStages: List<ExcludedItem> = emptyList()
    ): List<Quadruple<String, String, Color, EventStatus>>
}
