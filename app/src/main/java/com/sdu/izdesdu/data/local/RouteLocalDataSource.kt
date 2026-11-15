package com.sdu.izdesdu.data.local

import android.content.Context
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.domain.model.Question
import com.sdu.izdesdu.domain.model.Route

class RouteLocalDataSource(
    private val getString: (Int) -> String
) {
    constructor(context: Context) : this({ id -> context.getString(id) })

    fun loadRoutes(): List<Route> {
        val canteen = loadCanteenRoute()
        return listOf(canteen, libraryRoute(canteen), medicalRoute(canteen))
    }

    private fun loadCanteenRoute(): Route = Route(
            id = "gate_to_canteen",
            title = getString(R.string.route_gate_to_canteen_title),
            fromPlaceId = "main_gate",
            toPlaceId = "canteen",
            fromTitle = getString(R.string.place_main_gate),
            toTitle = getString(R.string.place_canteen),
            checkpoints = listOf(
                CheckPoint(
                    id = 1,
                    title = getString(R.string.cp_entrance_c_title),
                    instruction = getString(R.string.cp_entrance_c_instruction),
                    mapX = 180f,
                    mapY = 520f,
                    question = Question(
                        text = getString(R.string.q_founded_year_text),
                        options = listOf(
                            getString(R.string.q_founded_year_opt1),
                            getString(R.string.q_founded_year_opt2),
                            getString(R.string.q_founded_year_opt3),
                            getString(R.string.q_founded_year_opt4)
                        ),
                        correctIndex = 1,
                        hint = getString(R.string.q_founded_year_hint)
                    )
                ),
                CheckPoint(
                    id = 2,
                    title = getString(R.string.cp_hall_title),
                    instruction = getString(R.string.cp_hall_instruction),
                    mapX = 180f,
                    mapY = 440f,
                    question = Question(
                        text = getString(R.string.q_named_after_text),
                        options = listOf(
                            getString(R.string.q_named_after_opt1),
                            getString(R.string.q_named_after_opt2),
                            getString(R.string.q_named_after_opt3),
                            getString(R.string.q_named_after_opt4)
                        ),
                        correctIndex = 2,
                        hint = getString(R.string.q_named_after_hint)
                    )
                ),
                CheckPoint(
                    id = 3,
                    title = getString(R.string.cp_block_d_title),
                    instruction = getString(R.string.cp_block_d_instruction),
                    mapX = 160f,
                    mapY = 360f,
                    question = Question(
                        text = getString(R.string.q_language_text),
                        options = listOf(
                            getString(R.string.q_language_opt1),
                            getString(R.string.q_language_opt2),
                            getString(R.string.q_language_opt3),
                            getString(R.string.q_language_opt4)
                        ),
                        correctIndex = 0,
                        hint = getString(R.string.q_language_hint)
                    )
                ),
                CheckPoint(
                    id = 4,
                    title = getString(R.string.cp_lecture_halls_title),
                    instruction = getString(R.string.cp_lecture_halls_instruction),
                    mapX = 220f,
                    mapY = 290f,
                    question = Question(
                        text = getString(R.string.q_city_text),
                        options = listOf(
                            getString(R.string.q_city_opt1),
                            getString(R.string.q_city_opt2),
                            getString(R.string.q_city_opt3),
                            getString(R.string.q_city_opt4)
                        ),
                        correctIndex = 1,
                        hint = getString(R.string.q_city_hint)
                    )
                ),
                CheckPoint(
                    id = 5,
                    title = getString(R.string.cp_canteen_title),
                    instruction = getString(R.string.cp_canteen_instruction),
                    mapX = 230f,
                    mapY = 220f,
                    question = Question(
                        text = getString(R.string.q_last_block_text),
                        options = listOf(
                            getString(R.string.q_last_block_opt1),
                            getString(R.string.q_last_block_opt2),
                            getString(R.string.q_last_block_opt3),
                            getString(R.string.q_last_block_opt4)
                        ),
                        correctIndex = 2,
                        hint = getString(R.string.q_last_block_hint)
                    )
                )
            )
        )


    private fun libraryRoute(canteen: Route): Route = canteen.copy(
        id = "gate_to_library",
        title = getString(R.string.route_library_title),
        toPlaceId = "library",
        toTitle = getString(R.string.route_library_place),
        checkpoints = canteen.checkpoints.take(2) + CheckPoint(
            id = 3,
            title = getString(R.string.route_library_place),
            instruction = getString(R.string.route_library_instruction),
            mapX = 220f,
            mapY = 420f,
            question = Question(
                text = getString(R.string.route_library_question),
                options = listOf("A", "B", "F", "I"),
                correctIndex = 1,
                hint = getString(R.string.route_library_hint)
            )
        )
    )

    private fun medicalRoute(canteen: Route): Route = canteen.copy(
        id = "gate_to_medical",
        title = getString(R.string.route_medical_title),
        toPlaceId = "medical",
        toTitle = getString(R.string.route_medical_place),
        checkpoints = canteen.checkpoints.take(3) + listOf(
            canteen.checkpoints[3].copy(
                title = getString(R.string.route_block_g),
                instruction = getString(R.string.route_block_g_instruction),
                mapX = 180f,
                mapY = 170f
            ),
            CheckPoint(
                id = 5,
                title = getString(R.string.route_medical_place),
                instruction = getString(R.string.route_medical_instruction),
                mapX = 210f,
                mapY = 140f,
                question = Question(
                    text = getString(R.string.route_medical_question),
                    options = listOf("A и B", "C и D", "H и G", "E и F"),
                    correctIndex = 2,
                    hint = getString(R.string.route_medical_hint)
                )
            )
        )
    )
}
