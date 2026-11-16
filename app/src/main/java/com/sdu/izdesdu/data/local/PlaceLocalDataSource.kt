package com.sdu.izdesdu.data.local

import android.content.Context
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.Place

class PlaceLocalDataSource(
    private val getString: (Int) -> String = { "" }
) {
    constructor(context: Context) : this({ id -> context.getString(id) })

    fun loadPlaces(): List<Place> = listOf(
        Place(
            id = "block_i",
            title = getString(R.string.place_block_i_title),
            block = getString(R.string.place_block_i_block),
            description = getString(R.string.place_block_i_desc),
            nearby = listOf(
                getString(R.string.nearby_block_h),
                getString(R.string.nearby_main_corridor)
            ),
            questionsCount = 6,
            colorHex = 0xFFA8642A,
            mapX = 94.4f,
            mapY = 235.9f
        ),
        Place(
            id = "medical",
            title = getString(R.string.place_medical_title),
            block = getString(R.string.place_medical_block),
            description = getString(R.string.place_medical_desc),
            nearby = listOf(
                getString(R.string.nearby_block_h),
                getString(R.string.nearby_block_g),
                getString(R.string.nearby_main_corridor)
            ),
            questionsCount = 5,
            colorHex = 0xFFD9366B,
            mapX = 118.4f,
            mapY = 357.8f
        ),
        Place(
            id = "canteen",
            title = getString(R.string.place_canteen),
            block = getString(R.string.place_canteen_block),
            description = getString(R.string.place_canteen_desc),
            nearby = listOf(
                getString(R.string.nearby_coffee_shop),
                getString(R.string.nearby_tables_zone),
                getString(R.string.nearby_block_f_rooms)
            ),
            questionsCount = 5,
            colorHex = 0xFFE0791B,
            mapX = 163.2f,
            mapY = 462.8f
        ),
        Place(
            id = "halls",
            title = getString(R.string.place_halls_title),
            block = getString(R.string.place_halls_block),
            description = getString(R.string.place_halls_desc),
            nearby = listOf(
                getString(R.string.nearby_block_e),
                getString(R.string.nearby_block_d),
                getString(R.string.nearby_canteen)
            ),
            questionsCount = 4,
            colorHex = 0xFF1F9D6B,
            mapX = 162.6f,
            mapY = 582.8f
        ),
        Place(
            id = "library",
            title = getString(R.string.place_library_title),
            block = getString(R.string.place_library_block),
            description = getString(R.string.place_library_desc),
            nearby = listOf(
                getString(R.string.nearby_hall),
                getString(R.string.nearby_block_a),
                getString(R.string.nearby_entrance_c)
            ),
            questionsCount = 3,
            colorHex = 0xFF2F66D8,
            mapX = 105.6f,
            mapY = 741.2f
        ),
        Place(
            id = "dorm",
            title = getString(R.string.place_dorm_title),
            block = getString(R.string.place_dorm_block),
            description = getString(R.string.place_dorm_desc),
            nearby = listOf(
                getString(R.string.nearby_sports_grounds),
                getString(R.string.nearby_main_building)
            ),
            questionsCount = 3,
            colorHex = 0xFF3D7A2E,
            mapX = 323.6f,
            mapY = 556.4f
        ),
        Place(
            id = "gym",
            title = getString(R.string.place_gym_title),
            block = getString(R.string.place_gym_block),
            description = getString(R.string.place_gym_desc),
            nearby = listOf(
                getString(R.string.nearby_sports_grounds),
                getString(R.string.nearby_dorm)
            ),
            questionsCount = 4,
            colorHex = 0xFFC98A1E,
            mapX = 309.8f,
            mapY = 156.3f
        )
    )
}
