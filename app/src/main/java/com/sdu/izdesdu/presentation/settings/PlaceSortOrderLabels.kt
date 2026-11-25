package com.sdu.izdesdu.presentation.settings

import androidx.annotation.StringRes
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.PlaceSortOrder

@get:StringRes
internal val PlaceSortOrder.titleRes: Int
    get() = when (this) {
        PlaceSortOrder.DEFAULT -> R.string.sort_default
        PlaceSortOrder.TITLE_ASC -> R.string.sort_title
        PlaceSortOrder.QUESTIONS_ASC -> R.string.sort_short
        PlaceSortOrder.QUESTIONS_DESC -> R.string.sort_long
    }
