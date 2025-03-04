package org.saudigitus.rei.data.source

interface EnrollmentRepository {
    suspend fun createEnrollment(
        ou: String,
        program: String
    ): String?
}