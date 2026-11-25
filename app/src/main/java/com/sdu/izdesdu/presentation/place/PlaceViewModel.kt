package com.sdu.izdesdu.presentation.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.domain.usecase.AddNoteUseCase
import com.sdu.izdesdu.domain.usecase.DeleteNoteUseCase
import com.sdu.izdesdu.domain.usecase.GetPlaceByIdUseCase
import com.sdu.izdesdu.domain.usecase.GetPlaceNotesUseCase
import com.sdu.izdesdu.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class PlaceViewModel(
    val placeId: String,
    getPlaceByIdUseCase: GetPlaceByIdUseCase,
    getPlaceNotesUseCase: GetPlaceNotesUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val updateNoteUseCase: UpdateNoteUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    dispatcher: CoroutineDispatcher? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaceUiState(place = getPlaceByIdUseCase(placeId)))
    val uiState: StateFlow<PlaceUiState> = _uiState.asStateFlow()

    val launchContext = dispatcher ?: EmptyCoroutineContext

    init {
        viewModelScope.launch(launchContext) {
            getPlaceNotesUseCase(placeId).collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }

    fun addNote(text: String) {
        viewModelScope.launch(launchContext) {
            addNoteUseCase(placeId, text)
        }
    }

    fun updateNote(note: Note, text: String) {
        viewModelScope.launch(launchContext) {
            updateNoteUseCase(note, text)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(launchContext) {
            deleteNoteUseCase(note)
        }
    }
}
