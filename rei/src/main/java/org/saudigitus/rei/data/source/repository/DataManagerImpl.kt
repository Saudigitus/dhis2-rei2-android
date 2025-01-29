package org.saudigitus.rei.data.source.repository

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.dhis2.bindings.userFriendlyValue
import org.dhis2.commons.network.NetworkUtils
import org.dhis2.commons.resources.ResourceManager
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScope
import org.hisp.dhis.android.core.enrollment.EnrollmentStatus
import org.hisp.dhis.android.core.event.EventStatus
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttribute
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValue
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance
import org.saudigitus.rei.data.model.SearchTeiModel
import org.saudigitus.rei.data.model.Stage
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.utils.countEventsByStatusToday
import org.saudigitus.rei.utils.overdueEventCount
import org.saudigitus.rei.utils.reiModuleDatastore
import javax.inject.Inject

class DataManagerImpl
@Inject constructor(
    private val d2: D2,
    private val networkUtils: NetworkUtils,
    private val resourceManager: ResourceManager,
) : DataManager {

    private lateinit var currentProgram: String
    private val orgUnitNameCache = mutableMapOf<String, String?>()

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

    override suspend fun getTeis(
        program: String,
        stage: String?,
        eventDate: String?,
    ) = withContext(Dispatchers.IO) {
        val repository = d2.trackedEntityModule().trackedEntityInstanceQuery()

        return@withContext if (networkUtils.isOnline()) {
            repository.offlineFirst().allowOnlineCache().eq(true)
                .byProgram().eq(program)
                .byProgramStage().eq("$stage")
                .blockingGet()
                .flatMap { tei -> listOf(tei) }
                .map { tei -> transform(tei, program) }
        } else {
            repository.offlineOnly().allowOnlineCache().eq(false)
                .byProgram().eq(program)
                .byProgramStage().eq("$stage")
                .blockingGet()
                .flatMap { tei -> listOf(tei) }
                .map { tei -> transform(tei, program) }
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

    private fun transform(
        tei: TrackedEntityInstance?,
        program: String?,
    ): SearchTeiModel {
        val searchTei = SearchTeiModel()
        searchTei.tei = tei
        currentProgram = program ?: ""

        if (tei?.trackedEntityAttributeValues() != null) {
            if (program != null) {
                val programAttributes = d2.programModule().programTrackedEntityAttributes()
                    .byProgram().eq(program)
                    .byDisplayInList().isTrue
                    .orderBySortOrder(RepositoryScope.OrderByDirection.ASC)
                    .blockingGet()

                for (programAttribute in programAttributes) {
                    val attribute = d2.trackedEntityModule().trackedEntityAttributes()
                        .uid(programAttribute.trackedEntityAttribute()!!.uid())
                        .blockingGet()

                    for (attrValue in tei.trackedEntityAttributeValues()!!) {
                        if (attrValue.trackedEntityAttribute() == attribute?.uid()) {
                            addAttribute(searchTei, attrValue, attribute)
                            break
                        }
                    }
                }
            } else {
                val typeAttributes = d2.trackedEntityModule().trackedEntityTypeAttributes()
                    .byTrackedEntityTypeUid().eq(searchTei.tei.trackedEntityType())
                    .byDisplayInList().isTrue
                    .blockingGet()
                for (typeAttribute in typeAttributes) {
                    val attribute = d2.trackedEntityModule().trackedEntityAttributes()
                        .uid(typeAttribute.trackedEntityAttribute()!!.uid())
                        .blockingGet()
                    for (attrValue in tei.trackedEntityAttributeValues()!!) {
                        if (attrValue.trackedEntityAttribute() == attribute?.uid()) {
                            addAttribute(searchTei, attrValue, attribute)
                            break
                        }
                    }
                }
            }

            val enrollments = d2.enrollmentModule().enrollments()
                .byTrackedEntityInstance().eq(tei.uid())
                .byProgram().eq("$program")
                .orderByEnrollmentDate(RepositoryScope.OrderByDirection.DESC)
                .blockingGet()

            if (enrollments.isNotEmpty()) {
                for (enrollment in enrollments) {
                    if (enrollment.status() == EnrollmentStatus.ACTIVE) {
                        searchTei.setCurrentEnrollment(enrollment)
                        break
                    }
                }
            }

            if (searchTei.selectedEnrollment == null) {
                searchTei.setCurrentEnrollment(enrollments[0])
            }

            for (enrollment in enrollments) {
                searchTei.addEnrollment(enrollment)
            }

            if (searchTei.selectedEnrollment != null) {
                searchTei.enrolledOrgUnit =
                    orgUnitName(searchTei.selectedEnrollment.organisationUnit()!!)
            } else {
                searchTei.enrolledOrgUnit = orgUnitName(searchTei.tei.organisationUnit()!!)
            }
        }

        searchTei.displayOrgUnit = displayOrgUnit()
        return searchTei
    }

    private fun addAttribute(
        searchTei: SearchTeiModel,
        attrValue: TrackedEntityAttributeValue,
        attribute: TrackedEntityAttribute?,
    ) {
        val friendlyValue = attrValue.userFriendlyValue(d2)

        val attrValueBuilder = TrackedEntityAttributeValue.builder()
        attrValueBuilder.value(friendlyValue)
            .created(attrValue.created())
            .lastUpdated(attrValue.lastUpdated())
            .trackedEntityAttribute(attrValue.trackedEntityAttribute())
            .trackedEntityInstance(searchTei.tei.uid())
        searchTei.addAttributeValue(attribute?.displayFormName(), attrValueBuilder.build())
    }

    private fun orgUnitName(orgUnitUid: String): String? {
        if (!orgUnitNameCache.containsKey(orgUnitUid)) {
            val organisationUnit = d2.organisationUnitModule()
                .organisationUnits()
                .uid(orgUnitUid)
                .blockingGet()
            orgUnitNameCache[orgUnitUid] = organisationUnit!!.displayName()
        }
        return orgUnitNameCache[orgUnitUid]
    }

    private fun displayOrgUnit(): Boolean {
        return d2.organisationUnitModule().organisationUnits()
            .byProgramUids(listOf(currentProgram))
            .blockingGet().size > 1
    }
}
