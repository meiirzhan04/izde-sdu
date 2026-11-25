package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sdu.izdesdu.R
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary

@Composable
fun NoteEditorDialog(
    initialText: String,
    isNewNote: Boolean,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf(initialText) }
    val title = if (isNewNote) R.string.place_note_new_title else R.string.place_note_edit_title

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(text = stringResource(title), color = IzdeTextPrimary)
        },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(text = stringResource(R.string.place_note_hint)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(text) },
                enabled = text.isNotBlank()
            ) {
                Text(text = stringResource(R.string.place_note_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.place_note_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun NoteEditorDialogPreview() {
    NoteEditorDialog(
        initialText = "Выдача книг до 18:00",
        isNewNote = false,
        onSave = {},
        onDismiss = {}
    )
}
