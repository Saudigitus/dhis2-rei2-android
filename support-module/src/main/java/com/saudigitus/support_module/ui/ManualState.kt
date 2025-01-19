package com.saudigitus.support_module.ui

import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState
import com.saudigitus.support_module.data.models.manuals.ManualItem
import java.io.File

data class ManualsUiState(
    val isLoading: Boolean = false,
    val hasFileLoaded: Boolean = false,
    val isDownloading: Boolean = false,
    val manualItems : List<ManualItem> = emptyList(),
    val pdf: File? = null,
    val pdfVerticalReaderState: VerticalPdfReaderState? = null
)