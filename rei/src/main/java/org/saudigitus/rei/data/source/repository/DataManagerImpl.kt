package org.saudigitus.rei.data.source.repository

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.dhis2.commons.resources.ResourceManager
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.event.EventStatus
import org.saudigitus.rei.data.model.Stage
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.utils.countEventsByStatusToday
import org.saudigitus.rei.utils.overdueEventCount
import org.saudigitus.rei.utils.reiModuleDatastore
import javax.inject.Inject

class DataManagerImpl
@Inject constructor(
    private val d2: D2,
    private val resourceManager: ResourceManager,
) : DataManager {
    override suspend fun loadConfig() = withContext(Dispatchers.IO) {
        return@withContext d2.reiModuleDatastore()
    }

    override suspend fun getStages(program: String) = withContext(Dispatchers.IO) {
        return@withContext d2.programModule().programStages()
            .byProgramUid().eq(program)
            .blockingGet()
            .map {
                Stage(
                    uid = it.uid(),
                    displayName = it.displayName(),
                )
            }
    }

    override suspend fun getStageEventData(
        program: String,
        stage: String,
    ) = withContext(Dispatchers.IO) {
        val (scheduledCount, completedCount, overdueCount) = awaitAll(
            async { d2.countEventsByStatusToday(program, stage, EventStatus.SCHEDULE) },
            async { d2.countEventsByStatusToday(program, stage, EventStatus.COMPLETED) },
            async { d2.overdueEventCount(program, stage) },
        )

        val stageStatus = loadConfig().find { it.program == program }?.stageItems
            ?.sortedBy { it.pos } ?: emptyList()

        return@withContext listOf(
            Triple("$scheduledCount", stageStatus[0].label, Color(resourceManager.getColorFrom(stageStatus[0].color))),
            Triple("$completedCount", stageStatus[1].label, Color(resourceManager.getColorFrom(stageStatus[1].color))),
            Triple("$overdueCount", stageStatus[2].label, Color(resourceManager.getColorFrom(stageStatus[2].color))),
        )
    }
}
