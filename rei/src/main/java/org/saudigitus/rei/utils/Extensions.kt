package org.saudigitus.rei.utils

import org.dhis2.commons.date.DateUtils
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScope
import org.hisp.dhis.android.core.event.EventStatus
import org.saudigitus.rei.data.model.AppConfigItem
import org.saudigitus.rei.utils.Utils.buildListFromJson
import java.util.Locale

fun String.capital() = this.replaceFirstChar {
    if (it.isLowerCase()) {
        it.titlecase(
            Locale.ENGLISH,
        )
    } else {
        this
    }
}

fun D2.countEventsByStatusToday(
    program: String,
    stage: String,
    eventStatus: EventStatus,
): Int = eventModule().events()
    .byProgramUid().eq(program)
    .byProgramStageUid().eq(stage)
    .byEventDate().eq(DateUtils.getInstance().today)
    .byStatus().eq(eventStatus)
    .blockingCount()

fun D2.overdueEventCount(
    program: String,
    stage: String,
): Int = eventModule().events()
    .byProgramUid().eq(program)
    .byProgramStageUid().eq(stage)
    .byDueDate().before(DateUtils.getInstance().today)
    .blockingCount()

fun D2.eventOrderedByDateDesc(enrollment: String) = eventModule().events()
    .byEnrollmentUid().eq(enrollment)
    .orderByEventDate(RepositoryScope.OrderByDirection.DESC)
    .one().blockingGet()

fun D2.reiModuleDatastore(): List<AppConfigItem> {
    val datastore = dataStoreModule().dataStore()
        .byNamespace().eq(Constants.NAMESPACE)
        .byKey().eq(Constants.KEY)
        .one().blockingGet()
    return buildListFromJson<AppConfigItem>(datastore?.value()) ?: emptyList()
}

fun D2.isEventOverdue(
    enrollment: String,
    stage: String,
) = eventModule().events()
    .byEnrollmentUid().eq(enrollment)
    .byProgramStageUid().eq(stage)
    .byDueDate().before(DateUtils.getInstance().today)
    .blockingCount() > 0
