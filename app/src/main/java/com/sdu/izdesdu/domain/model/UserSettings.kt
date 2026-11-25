package com.sdu.izdesdu.domain.model

data class UserSettings(
    val isDarkTheme: Boolean = false,
    val placeSortOrder: PlaceSortOrder = PlaceSortOrder.DEFAULT
)
