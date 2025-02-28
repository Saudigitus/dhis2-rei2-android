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
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.ui.components.StageTabState
import org.saudigitus.rei.ui.components.ToolbarHeaders
import org.saudigitus.rei.ui.mapper.TEICardMapper
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DataManager,
    private val teiCardMapper: TEICardMapper,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeUIState())

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value,
        )

    private val _program = MutableStateFlow("")
    val program: StateFlow<String> = _program

    init {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    teiCardMapper = teiCardMapper,
                )
            }
        }
    }

    private fun loadStages() {
        viewModelScope.launch {
            val stages = repository.getStages(program.value)

            viewModelState.update {
                it.copy(
                    stageTabState = StageTabState(
                        stages = stages,
                        stagesData = repository.getStageEventData(
                            program.value,
                            stages.firstOrNull()?.uid ?: "",
                        ),
                    ),
                    teis = if (stages.isNotEmpty()) {
                        repository.getTeis(
                            program = program.value,
                            stage = stages[0].uid,
                            eventDate = null,
                        )
                    } else emptyList(),
                )
            }
        }
    }

    fun setBundle(bundle: Bundle?) {
        _program.value = bundle?.getString(Constants.PROGRAM_UID) ?: ""
        loadStages()

        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    toolbarHeaders = ToolbarHeaders(title = "${bundle?.getString(DATA_SET_NAME)}"),
                    config = repository.loadConfig().find { data -> data.program == program.value },
                )
            }
        }
    }

    fun loadStageData(stage: String) {
        viewModelScope.launch {
            val stageState = viewModelState.value.stageTabState

            viewModelState.update {
                it.copy(
                    stageTabState = StageTabState(
                        stages = stageState?.stages ?: emptyList(),
                        stagesData = repository.getStageEventData(program.value, stage),
                    ),
                    teis = if (stageState?.stages?.isNotEmpty() == true) {
                        repository.getTeis(
                            program = program.value,
                            stage = stage,
                            eventDate = null,
                        )
                    } else emptyList(),
                )
            }
        }
    }
}
