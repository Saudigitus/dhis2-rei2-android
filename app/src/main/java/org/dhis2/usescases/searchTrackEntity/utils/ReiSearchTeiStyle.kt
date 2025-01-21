package org.dhis2.usescases.searchTrackEntity.utils

import androidx.compose.ui.graphics.Color
import io.ktor.client.utils.EmptyContent.status
import org.dhis2.commons.resources.ColorUtils
import org.dhis2.usescases.searchTrackEntity.SearchTeiModel
import org.hisp.dhis.android.core.D2
import org.saudigitus.rei.R
import org.saudigitus.rei.data.model.AppConfigItem
import org.saudigitus.rei.data.model.LineListing
import org.saudigitus.rei.data.model.Status
import org.saudigitus.rei.data.model.StatusKey
import org.saudigitus.rei.ui.theme.Light_Error
import org.saudigitus.rei.ui.theme.Light_Success
import org.saudigitus.rei.ui.theme.Light_Warning
import org.saudigitus.rei.utils.eventOrderedByDateDesc
import org.saudigitus.rei.utils.getByKey
import org.saudigitus.rei.utils.isEventOverdue
import org.saudigitus.rei.utils.reiModuleDatastore

class ReiSearchTeiStyle(private val d2: D2) {

    private var appConfigs: List<AppConfigItem> = emptyList()
    private var colorUtils: ColorUtils

    init {
        appConfigs = d2.reiModuleDatastore()
        colorUtils = ColorUtils()
    }

    private fun isCompletelyVaccinated(enrollment: String, lineListing: LineListing): Boolean {
        val event = d2.eventOrderedByDateDesc(enrollment)

        val eventValue = d2.trackedEntityModule().trackedEntityDataValues()
            .byEvent().eq(event?.uid())
            .byDataElement().eq(lineListing.ccvDataElement)
            .one().blockingGet()

        return eventValue?.value()?.toBoolean() == true
    }

    private fun getColorByStatus(status: List<Status>, statusKey: String): Color {
        val color = status.getByKey(statusKey)?.color
        return if (!color.isNullOrEmpty()) {
            Color(colorUtils.parseColor(color))
        } else {
           Color.Transparent
        }
    }

    fun getTeiCardBackground(searchTeiModel: SearchTeiModel): Pair<String, Color> {
        val enrollment = searchTeiModel.enrollments.getOrNull(0)
        val lineListing = appConfigs[0].lineListing
        val status = lineListing.status

        return if (enrollment != null) {
            val isOverdue = d2.isEventOverdue(enrollment.uid(), lineListing.stageVaccination)

            if (isOverdue) {
                Pair(status.getByKey(StatusKey.LATE)?.label ?: "", getColorByStatus(status, StatusKey.LATE))
            } else if (isCompletelyVaccinated(enrollment.uid(), lineListing)) {
                Pair(status.getByKey(StatusKey.DONE)?.label ?: "", getColorByStatus(status, StatusKey.DONE))
            } else {
                Pair(status.getByKey(StatusKey.NONE)?.label ?: "", getColorByStatus(status, StatusKey.NONE))
            }
        } else {
            Pair(status.getByKey(StatusKey.ACTIVE)?.label ?: "", Color.Transparent)
        }
    }
}