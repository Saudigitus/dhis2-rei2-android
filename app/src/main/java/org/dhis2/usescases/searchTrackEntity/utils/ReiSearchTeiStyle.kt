package org.dhis2.usescases.searchTrackEntity.utils

import androidx.compose.ui.graphics.Color
import org.dhis2.usescases.searchTrackEntity.SearchTeiModel
import org.hisp.dhis.android.core.D2
import org.saudigitus.rei.R
import org.saudigitus.rei.data.model.AppConfigItem
import org.saudigitus.rei.data.model.LineListing
import org.saudigitus.rei.ui.theme.Light_Error
import org.saudigitus.rei.ui.theme.Light_Success
import org.saudigitus.rei.ui.theme.Light_Warning
import org.saudigitus.rei.utils.eventOrderedByDateDesc
import org.saudigitus.rei.utils.isEventOverdue
import org.saudigitus.rei.utils.reiModuleDatastore

class ReiSearchTeiStyle(private val d2: D2) {

    private var appConfigs: List<AppConfigItem> = emptyList()

    init {
        appConfigs = d2.reiModuleDatastore()
    }

    private fun isCompletelyVaccinated(enrollment: String, lineListing: LineListing): Boolean {
        val event = d2.eventOrderedByDateDesc(enrollment)

        val eventValue = d2.trackedEntityModule().trackedEntityDataValues()
            .byEvent().eq(event?.uid())
            .byDataElement().eq(lineListing.ccvDataElement)
            .one().blockingGet()

        return eventValue?.value()?.toBoolean() == true
    }

    fun getTeiCardBackground(searchTeiModel: SearchTeiModel): Pair<Int, Color> {
        val enrollment = searchTeiModel.enrollments.getOrNull(0)
        val lineListing = appConfigs[0].lineListing

        return if (enrollment != null) {
            val isOverdue = d2.isEventOverdue(enrollment.uid(), lineListing.stageVaccination)

            if (isOverdue) {
                Pair(R.string.overdue, Light_Warning.copy(.2f))
            } else if (isCompletelyVaccinated(enrollment.uid(), lineListing)) {
                Pair(R.string.vaccinated, Light_Success.copy(.2f))
            } else {
                Pair(R.string.not_vaccinated, Light_Error.copy(.2f))
            }
        } else {
            Pair(R.string.active, Color.Transparent)
        }
    }
}