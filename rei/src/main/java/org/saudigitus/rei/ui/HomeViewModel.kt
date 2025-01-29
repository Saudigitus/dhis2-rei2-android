package org.saudigitus.rei.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.saudigitus.rei.data.model.SearchTeiModel
import org.saudigitus.rei.data.source.DataManager
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DataManager
): ViewModel() {

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
}