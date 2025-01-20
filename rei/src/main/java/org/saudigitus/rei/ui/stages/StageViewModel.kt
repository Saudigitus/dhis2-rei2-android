package org.saudigitus.rei.ui.stages

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
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.ui.components.ToolbarHeaders
import javax.inject.Inject

@HiltViewModel
class StageViewModel @Inject constructor(
    private val dataManager: DataManager,
) : ViewModel() {

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

    private fun loadConfig(program: String) {
        viewModelScope.launch {
            _config.value = dataManager.loadConfig().find { it.program == program }
        }
    }

    private fun loadStages() {
        viewModelScope.launch {
            val stages = dataManager.getStages(program.value)

            viewModelState.update {
                it.copy(
                    stages = stages,
                    stagesData = dataManager.getStageEventData(
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
                it.copy(stagesData = dataManager.getStageEventData(program.value, stage))
            }
        }
    }
}
