package com.saudigitus.support_module.ui.manualScreen

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState
import com.saudigitus.support_module.data.local.ManualsRepository
import com.saudigitus.support_module.ui.ManualsUiState
import com.saudigitus.support_module.utils.Constants.NO_MANUAL_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ManualViewModel @Inject internal constructor(
    private val manualsRepository: ManualsRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ManualsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getManualsFromDataStore()
    }

    private val _pdfVerticalReaderState = MutableStateFlow(
        VerticalPdfReaderState(
            resource = ResourceType.Local(Uri.parse("")),
            isZoomEnable = true,
        ),
    )
    val pdfVerticalReaderState = _pdfVerticalReaderState.asStateFlow()

    private fun getManualsFromDataStore() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDownloading = true) }
            manualsRepository.getManualsDataStore().collect { manuals ->
                manuals.forEach {
                    manualsRepository.downloadManualToLocal(context = context, url = it.path ?: "", fileName = it.uid)
                }
                _uiState.update {
                    it.copy(manualItems = manuals, isDownloading = false)
                }
            }
        }
    }

    fun openPdf(file: File) {
        _pdfVerticalReaderState.value = VerticalPdfReaderState(
            resource = ResourceType.Local(Uri.fromFile(file)),
            isZoomEnable = true,
        )
        _uiState.update { it.copy(hasFileLoaded = true) }
    }

    fun open(context: Context, fileName: String): File {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$fileName.pdf")
        if (!file.exists()) {
            Toast.makeText(context, NO_MANUAL_MESSAGE, Toast.LENGTH_SHORT).show()
        }
        return file
    }
}
