package com.saudigitus.support_module.ui.manualScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.rizzi.bouquet.VerticalPDFReader
import com.saudigitus.support_module.R
import com.saudigitus.support_module.ui.components.BasicApp
import java.io.File

@Composable
fun PdfViewer(
    navController: NavHostController,
    onBack: () -> Unit = {}, // Placeholder for back action
    viewModel: ManualViewModel = hiltViewModel(),
    file: File?,
) {
    val pdfVerticalReaderState by viewModel.pdfVerticalReaderState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(file) {
        if (file != null) {
            viewModel.openPdf(file)
        }
    }
    BasicApp(
        title = stringResource(id = R.string.user_manual_title),
        onBack = onBack,
        content = {
            if (uiState.hasFileLoaded) {
                VerticalPDFReader(
                    state = pdfVerticalReaderState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White),
                )
            }
        },
    )
}
