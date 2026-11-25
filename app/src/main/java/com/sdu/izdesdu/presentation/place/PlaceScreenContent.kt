package com.sdu.izdesdu.presentation.place

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.presentation.components.CampusMap
import com.sdu.izdesdu.presentation.components.NoteEditorDialog
import com.sdu.izdesdu.presentation.components.PlaceDetailsSheet
import com.sdu.izdesdu.presentation.components.ScreenMessage

@Composable
fun PlaceScreenContent(
    uiState: PlaceUiState,
    onBack: () -> Unit,
    onStartRoute: (String) -> Unit,
    onAddNote: (String) -> Unit,
    onUpdateNote: (Note, String) -> Unit,
    onDeleteNote: (Note) -> Unit
) {
    val place = uiState.place
    if (place == null) {
        ScreenMessage(
            message = R.string.place_not_found,
            onBack = onBack,
            messageColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    var isEditorOpen by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CampusMap(
                places = listOf(place),
                selectedId = place.id,
                onPinClick = {},
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.route_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        PlaceDetailsSheet(
            place = place,
            notes = uiState.notes,
            onStartRoute = onStartRoute,
            onAddNote = {
                editingNote = null
                isEditorOpen = true
            },
            onEditNote = { note ->
                editingNote = note
                isEditorOpen = true
            },
            onDeleteNote = onDeleteNote
        )
    }

    if (isEditorOpen) {
        val note = editingNote
        NoteEditorDialog(
            initialText = note?.text.orEmpty(),
            isNewNote = note == null,
            onSave = { text ->
                if (note == null) onAddNote(text) else onUpdateNote(note, text)
                isEditorOpen = false
            },
            onDismiss = { isEditorOpen = false }
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun PlaceScreenContentPreview() {
    val place = Place(
        id = "canteen",
        title = "Столовая",
        block = "Блок F",
        description = "Здесь обедают студенты. Рядом кофейня, а за ней зона со столиками.",
        nearby = listOf("Кофейня", "Зона со столиками", "Аудитории блока F"),
        questionsCount = 5,
        colorHex = 0xFFE0791B,
        mapX = 163.2f,
        mapY = 462.8f
    )
    PlaceScreenContent(
        uiState = PlaceUiState(
            place = place,
            notes = listOf(
                Note(id = 1, placeId = "canteen", text = "Кофе дешевле до 10:00", updatedAt = 0)
            )
        ),
        onBack = {},
        onStartRoute = {},
        onAddNote = {},
        onUpdateNote = { _, _ -> },
        onDeleteNote = {}
    )
}
