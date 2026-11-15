package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdu.izdesdu.presentation.components.AnswerOption
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.Question
import com.sdu.izdesdu.presentation.theme.IzdeError
import com.sdu.izdesdu.presentation.theme.IzdeErrorBg
import com.sdu.izdesdu.presentation.theme.IzdeGreen
import com.sdu.izdesdu.presentation.theme.IzdeHintBg
import com.sdu.izdesdu.presentation.theme.IzdeMintBg
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

private val optionLetters = listOf("А", "Б", "В", "Г")

@Composable
fun QuestionCard(
    selectedIndex: Int?,
    isCorrect: Boolean?,
    showHint: Boolean,
    instruction: String,
    question: Question,
    onSelectAnswer: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (instruction.isNotBlank()) {
            Text(
                text = instruction,
                fontSize = 12.sp,
                color = IzdeTextSecondary
            )
        }

        Text(
            text = question.text,
            fontSize = 17.sp,
            lineHeight = 23.sp,
            fontWeight = FontWeight.Bold,
            color = IzdeTextPrimary
        )

        question.options.forEachIndexed { index, optionText ->
            val isSelected = selectedIndex == index
            AnswerOption(
                letter = optionLetters.getOrElse(index) { ('A' + index).toString() },
                text = optionText,
                isSelected = isSelected,
                isRight = isSelected && isCorrect == true,
                isWrong = isSelected && isCorrect == false,
                enabled = isCorrect != true,
                onClick = { onSelectAnswer(index) }
            )
        }

        if (isCorrect != null) {
            FeedbackBanner(isCorrect = isCorrect == true)
        }

        if (showHint && isCorrect != true) {
            HintBanner(hint = question.hint)
        }
    }
}

@Composable
private fun HintBanner(hint: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = IzdeHintBg,
        border = BorderStroke(1.dp, IzdeOutline)
    ) {
        Text(
            text = stringResource(R.string.question_hint_prefix, hint),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = IzdeTextPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun FeedbackBanner(isCorrect: Boolean) {
    val bgColor = if (isCorrect) IzdeMintBg else IzdeErrorBg
    val fgColor = if (isCorrect) IzdeGreen else IzdeError

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                contentDescription = null,
                tint = fgColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(
                    if (isCorrect) R.string.question_correct_feedback else R.string.question_wrong_feedback
                ),
                color = fgColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
