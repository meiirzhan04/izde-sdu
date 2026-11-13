package com.sdu.izdesdu.data.repository

import androidx.compose.ui.graphics.Color
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.domain.repository.PlaceRepository

class PlaceRepositoryImpl : PlaceRepository {

    private val places = listOf(
        Place(
            id = "block_i",
            title = "Новый блок I",
            block = "Блок I",
            description = "Новый блок SDU. Находится над блоком H, в конце главного коридора.",
            nearby = listOf("Блок H", "Главный коридор"),
            questionsCount = 6,
            color = Color(0xFFA8642A),
            mapX = 94.4f,
            mapY = 235.9f
        ),
        Place(
            id = "medical",
            title = "Медпункт",
            block = "Между H и G",
            description = "Медпункт университета. Если на паре стало плохо, иди сюда.",
            nearby = listOf("Блок H", "Блок G", "Главный коридор"),
            questionsCount = 5,
            color = Color(0xFFD9366B),
            mapX = 118.4f,
            mapY = 357.8f
        ),
        Place(
            id = "canteen",
            title = "Столовая",
            block = "Блок F",
            description = "Здесь обедают студенты. Рядом кофейня, а за ней зона со столиками.",
            nearby = listOf("Кофейня", "Зона со столиками", "Аудитории блока F"),
            questionsCount = 5,
            color = Color(0xFFE0791B),
            mapX = 163.2f,
            mapY = 462.8f
        ),
        Place(
            id = "halls",
            title = "Лекционные залы",
            block = "Блоки E и D",
            description = "Большие круглые залы справа от главного коридора.",
            nearby = listOf("Блок E", "Блок D", "Столовая"),
            questionsCount = 4,
            color = Color(0xFF1F9D6B),
            mapX = 162.6f,
            mapY = 582.8f
        ),
        Place(
            id = "library",
            title = "Библиотека",
            block = "Блок B",
            description = "Библиотека SDU в блоке B, рядом с нижним холлом и входом C.",
            nearby = listOf("Холл", "Блок A", "Вход C"),
            questionsCount = 3,
            color = Color(0xFF2F66D8),
            mapX = 105.6f,
            mapY = 741.2f
        ),
        Place(
            id = "dorm",
            title = "Общежитие",
            block = "Student House",
            description = "Общежитие для студентов, справа от главного корпуса.",
            nearby = listOf("Спортплощадки", "Главный корпус"),
            questionsCount = 3,
            color = Color(0xFF3D7A2E),
            mapX = 323.6f,
            mapY = 556.4f
        ),
        Place(
            id = "gym",
            title = "Спортзал SDU Life",
            block = "SDU Life",
            description = "Спортзал рядом со спортивными площадками, за общежитием.",
            nearby = listOf("Спортплощадки", "Общежитие"),
            questionsCount = 4,
            color = Color(0xFFC98A1E),
            mapX = 309.8f,
            mapY = 156.3f
        )
    )

    override fun getPlaces(): List<Place> = places

    override fun getPlace(id: String): Place? = places.find { it.id == id }
}