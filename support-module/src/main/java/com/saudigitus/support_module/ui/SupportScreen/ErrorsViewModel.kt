package com.saudigitus.support_module.ui.SupportScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saudigitus.support_module.data.local.SyncErrorsRepository
import com.saudigitus.support_module.ui.errorsScreen.ErrorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ErrorsViewModel @Inject internal constructor(
    private val repository: SyncErrorsRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _errorsUiState = MutableStateFlow(ErrorUiState())
    val errorsUiState = _errorsUiState.asStateFlow()

    private fun getErrors() {
        viewModelScope.launch {
            _errorsUiState.update { it.copy(isLoading = true) }
            repository.getSyncErrors().collect { errors ->
                _errorsUiState.update {
                    it.copy(errorsItems = errors, isLoading = false)
                }
            }
        }
    }

    init {
        getErrors()
    }
}
