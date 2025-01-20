package com.saudigitus.support_module.ui.errorsScreen

import com.saudigitus.support_module.data.models.erros.ErrorModel

data class ErrorUiState(
    val isLoading: Boolean = false,
    val errorsItems: List<ErrorModel> = emptyList()
)