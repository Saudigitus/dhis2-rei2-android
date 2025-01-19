package com.saudigitus.support_module.data.local

import android.content.Context
import com.saudigitus.support_module.data.models.manuals.ManualItem
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import java.io.File

interface ManualsRepository {
    fun getManualsDataStore(): Flow<List<ManualItem>>
    suspend fun openManual(context: Context, url: String, fileName: String): File?
    suspend fun downloadManualToLocal(
        context: Context,
        url: String,
        fileName: String
    )
}