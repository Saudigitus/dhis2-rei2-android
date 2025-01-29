package org.saudigitus.rei.ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dhis2.commons.Constants
import org.dhis2.commons.Constants.DATA_SET_NAME
import org.saudigitus.rei.data.model.AppConfigItem
import org.saudigitus.rei.data.model.SearchTeiModel
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.ui.components.ToolbarHeaders
import org.saudigitus.rei.ui.components.StageTabState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DataManager
): ViewModel() {

    private val viewModelState = MutableStateFlow(StageTabState())

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value,
        )

    private val _toolbarHeaders = MutableStateFlow(
        ToolbarHeaders(title = ""),
    )
    val toolbarHeaders: StateFlow<ToolbarHeaders> = _toolbarHeaders

    private val _config = MutableStateFlow<AppConfigItem?>(null)
    val config: StateFlow<AppConfigItem?> = _config

    private val _program = MutableStateFlow("")
    val program: StateFlow<String> = _program

    private val _teis = MutableStateFlow<List<SearchTeiModel>>(emptyList())
    val teis: StateFlow<List<SearchTeiModel>> = _teis

    init {
        viewModelScope.launch {
            _teis.value = repository.getTeis(
                ou = "",
                program = "",
                stage = "",
                eventDate = null
            )
        }
    }

    private fun loadConfig(program: String) {
        viewModelScope.launch {
            _config.value = repository.loadConfig().find { it.program == program }
        }
    }

    private fun loadStages() {
        viewModelScope.launch {
            val stages = repository.getStages(program.value)

            viewModelState.update {
                it.copy(
                    stages = stages,
                    stagesData = repository.getStageEventData(
                        program.value,
                        stages.firstOrNull()?.uid ?: "",
                    ),
                )
            }
        }
    }

    fun setBundle(bundle: Bundle?) {
        _program.value = bundle?.getString(Constants.PROGRAM_UID) ?: ""
        loadConfig(program.value)
        loadStages()

        _toolbarHeaders.update {
            it.copy(title = "${bundle?.getString(DATA_SET_NAME)}")
        }
    }

    fun loadStageData(stage: String) {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(stagesData = repository.getStageEventData(program.value, stage))
            }
        }
    }
}