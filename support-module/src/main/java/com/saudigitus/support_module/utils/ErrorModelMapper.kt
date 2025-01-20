package com.saudigitus.support_module.utils

import com.saudigitus.support_module.data.models.erros.ErrorModel
import org.hisp.dhis.android.core.imports.TrackerImportConflict
import org.hisp.dhis.android.core.maintenance.D2Error
import org.hisp.dhis.android.core.maintenance.ForeignKeyViolation

class ErrorModelMapper(private val fkMessage: String) {

    companion object {
        const val FK = "FK"
    }

    fun mapD2Error(errors: List<D2Error>): List<ErrorModel> {
        return errors.map {
            map(it)
        }
    }

    fun map(error: D2Error): ErrorModel {
        return ErrorModel(
            error.created(),
            error.httpErrorCode().toString(),
            error.errorDescription(),
            error.errorComponent()?.name ?: "",
        )
    }

    fun mapConflict(conflicts: List<TrackerImportConflict>): List<ErrorModel> {
        return conflicts.map {
            map(it)
        }
    }

    fun map(conflict: TrackerImportConflict): ErrorModel {
        return ErrorModel(
            conflict.created(),
            conflict.errorCode(),
            conflict.displayDescription() ?: conflict.conflict(),
            conflict.status()?.name,
        )
    }

    fun mapFKViolation(fKViolations: List<ForeignKeyViolation>): List<ErrorModel> {
        return fKViolations.map {
            map(it)
        }
    }

    fun map(fKViolation: ForeignKeyViolation): ErrorModel {
        val toTable = fKViolation.toTable() ?: ""
        val fromTable = fKViolation.fromTable() ?: ""
        val toUid = fKViolation.notFoundValue() ?: ""
        val fromUid = fKViolation.fromObjectUid() ?: ""
        return ErrorModel(
            fKViolation.created(),
            FK,
            fkMessage.format(toTable, toUid, fromTable, fromUid),
            "",
        )
    }
}
