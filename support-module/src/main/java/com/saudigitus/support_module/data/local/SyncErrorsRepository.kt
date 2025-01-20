package com.saudigitus.support_module.data.local

import com.saudigitus.support_module.data.models.erros.ErrorModel
import kotlinx.coroutines.flow.Flow

interface SyncErrorsRepository {
    fun getSyncErrors(): Flow<List<ErrorModel>>
}