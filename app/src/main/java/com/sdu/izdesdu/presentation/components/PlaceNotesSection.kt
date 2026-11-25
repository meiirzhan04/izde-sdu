package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

@Composable
fun PlaceNotesSection(
    notes: List<Note>,
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.place_notes_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = IzdeTextPrimary,
                modifier = Modifier.weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onAddNote() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = IzdeBlue,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(R.string.place_note_add),
                    style = MaterialTheme.typography.labelLarge,
                    color = IzdeBlue
                )
            }
        }

        if (notes.isEmpty()) {
            Text(
                text = stringResource(R.string.place_notes_empty),
                style = MaterialTheme.typography.bodySmall,
                color = IzdeTextSecondary
            )
        } else {
            notes.forEach { note ->
                PlaceNoteItem(
                    note = note,
                    onEdit = { onEditNote(note) },
                    onDelete = { onDeleteNote(note) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceNotesSectionPreview() {
    PlaceNotesSection(
        notes = listOf(
            Note(id = 1, placeId = "library", text = "Тихий зал на 2 этаже", updatedAt = 0),
            Note(id = 2, placeId = "library", text = "Выдача книг до 18:00", updatedAt = 0)
        ),
        onAddNote = {},
        onEditNote = {},
        onDeleteNote = {}
    )
}
