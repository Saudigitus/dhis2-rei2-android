package org.saudigitus.rei.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dhis2.commons.Constants
import org.dhis2.commons.Constants.DATA_SET_NAME
import org.hisp.dhis.android.core.event.EventStatus
import org.saudigitus.rei.data.model.ExcludedItem
import org.saudigitus.rei.data.model.OU
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.data.source.EnrollmentRepository
import org.saudigitus.rei.ui.components.StageTabState
import org.saudigitus.rei.ui.components.ToolbarHeaders
import org.saudigitus.rei.ui.mapper.TEICardMapper
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DataManager,
    private val enrollmentRepository: EnrollmentRepository,
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

    private val _newEnrollment = MutableStateFlow("")
    val newEnrollment: StateFlow<String> = _newEnrollment

    init {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    isLoading = true,
                    teiCardMapper = teiCardMapper,
                )
            }
        }
    }

    fun generateEnrollment(ou: OU) {
        viewModelScope.launch {
            _newEnrollment.value = enrollmentRepository
                .createEnrollment(ou.uid, program.value) ?: ""
        }
    }

    private fun loadStages() {
        viewModelScope.launch {
            val stages = repository.getStages(program.value)
            val excludedStagesAsyc = async { excludedStages(stages.firstOrNull()?.uid ?: "") }
            val teisAsyc = async {
                repository.getTeis(
                    program = program.value,
                    stage = stages[0].uid,
                )
            }

            val excludedStages = excludedStagesAsyc.await()
            val teis = teisAsyc.await()

            val isLoading = !(excludedStagesAsyc.isCompleted && teisAsyc.isCompleted)

            viewModelState.update {
                it.copy(
                    isLoading = isLoading,
                    stageTabState = StageTabState(
                        stages = stages,
                        stagesData = repository.getStageEventData(
                            program.value,
                            stages.firstOrNull()?.uid ?: "",
                            excludedStages
                        ),
                    ),
                    teis = if (stages.isNotEmpty()) {
                        teis
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
                        stagesData = repository.getStageEventData(
                            program.value, stage, excludedStages(stage)
                        ),
                    ),
                    teis = if (stageState?.stages?.isNotEmpty() == true) {
                        repository.getTeis(
                            program = program.value,
                            stage = stage,
                        )
                    } else emptyList(),
                )
            }
        }
    }

    fun loadTEI(stage: String, eventStatus: EventStatus) {
        viewModelScope.launch {
            viewModelState.update { it.copy(isLoadingSection2 = true) }
            val stageState = viewModelState.value.stageTabState

            val teisAsyc = async {
                repository.getTeis(
                    program = program.value,
                    stage = stage,
                    eventStatus = eventStatus
                )
            }

            val teis = teisAsyc.await()

            viewModelState.update {
                it.copy(
                    isLoadingSection2 = !teisAsyc.isCompleted,
                    teis = if (stageState?.stages?.isNotEmpty() == true) {
                        teis
                    } else emptyList(),
                )
            }
        }
    }

    private fun excludedStages(stage: String): List<ExcludedItem> {
        return viewModelState.value.config?.stageItems?.mapNotNull {
            it.excludeFrom.find { removedStage -> removedStage.stage == stage  }
        } ?: emptyList()
    }
}
