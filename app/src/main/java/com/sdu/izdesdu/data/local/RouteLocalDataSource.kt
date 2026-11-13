package com.sdu.izdesdu.data.local

import android.content.Context
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.domain.model.Question
import com.sdu.izdesdu.domain.model.Route

class RouteLocalDataSource(
    private val context: Context
) {
    fun loadRoutes(): List<Route> = listOf(
        Route(
            id = "gate_to_canteen",
            title = context.getString(R.string.route_gate_to_canteen_title),
            fromPlaceId = "main_gate",
            toPlaceId = "canteen",
            checkpoints = listOf(
                CheckPoint(
                    id = 1,
                    title = context.getString(R.string.cp_entrance_c_title),
                    instruction = context.getString(R.string.cp_entrance_c_instruction),
                    mapX = 180f,
                    mapY = 520f,
                    question = Question(
                        text = context.getString(R.string.q_founded_year_text),
                        options = listOf(
                            context.getString(R.string.q_founded_year_opt1),
                            context.getString(R.string.q_founded_year_opt2),
                            context.getString(R.string.q_founded_year_opt3),
                            context.getString(R.string.q_founded_year_opt4)
                        ),
                        correctIndex = 1,
                        hint = context.getString(R.string.q_founded_year_hint)
                    )
                ),
                CheckPoint(
                    id = 2,
                    title = context.getString(R.string.cp_hall_title),
                    instruction = context.getString(R.string.cp_hall_instruction),
                    mapX = 180f,
                    mapY = 440f,
                    question = Question(
                        text = context.getString(R.string.q_named_after_text),
                        options = listOf(
                            context.getString(R.string.q_named_after_opt1),
                            context.getString(R.string.q_named_after_opt2),
                            context.getString(R.string.q_named_after_opt3),
                            context.getString(R.string.q_named_after_opt4)
                        ),
                        correctIndex = 2,
                        hint = context.getString(R.string.q_named_after_hint)
                    )
                ),
                CheckPoint(
                    id = 3,
                    title = context.getString(R.string.cp_block_d_title),
                    instruction = context.getString(R.string.cp_block_d_instruction),
                    mapX = 160f,
                    mapY = 360f,
                    question = Question(
                        text = context.getString(R.string.q_language_text),
                        options = listOf(
                            context.getString(R.string.q_language_opt1),
                            context.getString(R.string.q_language_opt2),
                            context.getString(R.string.q_language_opt3),
                            context.getString(R.string.q_language_opt4)
                        ),
                        correctIndex = 0,
                        hint = context.getString(R.string.q_language_hint)
                    )
                ),
                CheckPoint(
                    id = 4,
                    title = context.getString(R.string.cp_lecture_halls_title),
                    instruction = context.getString(R.string.cp_lecture_halls_instruction),
                    mapX = 220f,
                    mapY = 290f,
                    question = Question(
                        text = context.getString(R.string.q_city_text),
                        options = listOf(
                            context.getString(R.string.q_city_opt1),
                            context.getString(R.string.q_city_opt2),
                            context.getString(R.string.q_city_opt3),
                            context.getString(R.string.q_city_opt4)
                        ),
                        correctIndex = 1,
                        hint = context.getString(R.string.q_city_hint)
                    )
                ),
                CheckPoint(
                    id = 5,
                    title = context.getString(R.string.cp_canteen_title),
                    instruction = context.getString(R.string.cp_canteen_instruction),
                    mapX = 230f,
                    mapY = 220f,
                    question = Question(
                        text = context.getString(R.string.q_last_block_text),
                        options = listOf(
                            context.getString(R.string.q_last_block_opt1),
                            context.getString(R.string.q_last_block_opt2),
                            context.getString(R.string.q_last_block_opt3),
                            context.getString(R.string.q_last_block_opt4)
                        ),
                        correctIndex = 2,
                        hint = context.getString(R.string.q_last_block_hint)
                    )
                )
            )
        )
    )
}

