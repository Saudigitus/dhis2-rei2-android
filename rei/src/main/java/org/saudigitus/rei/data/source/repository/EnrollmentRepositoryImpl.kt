package org.saudigitus.rei.data.source.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.dhis2.commons.bindings.program
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.enrollment.EnrollmentCreateProjection
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCreateProjection
import org.saudigitus.rei.data.source.EnrollmentRepository
import javax.inject.Inject

class EnrollmentRepositoryImpl
@Inject constructor(
    private val d2: D2
) : EnrollmentRepository {
    override suspend fun createEnrollment(ou: String, program: String): String? =
        withContext(Dispatchers.IO) {
            d2.program(program)?.trackedEntityType()?.let { trackedEntityType ->
                d2.trackedEntityModule().trackedEntityInstances()
                    .blockingAdd(
                        TrackedEntityInstanceCreateProjection.builder()
                            .organisationUnit(ou)
                            .trackedEntityType(trackedEntityType.uid())
                            .build()
                    ).let { tei ->
                        return@withContext d2.enrollmentModule().enrollments()
                            .blockingAdd(
                                EnrollmentCreateProjection.builder()
                                    .organisationUnit(ou)
                                    .program(program)
                                    .trackedEntityInstance(tei)
                                    .build()
                            ) ?: null
                    }

            }
        }

}