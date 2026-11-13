package com.sdu.izdesdu.domain.model

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val hint: String
)