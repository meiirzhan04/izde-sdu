package com.sdu.izdesdu.presentation.question

import com.sdu.izdesdu.data.local.ProgressLocalDataSource
import com.sdu.izdesdu.data.repository.ProgressRepositoryImpl
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.domain.model.Question
import com.sdu.izdesdu.domain.model.Route
import com.sdu.izdesdu.domain.repository.RouteRepository
import com.sdu.izdesdu.domain.usecase.CheckAnswerUseCase
import com.sdu.izdesdu.domain.usecase.CompleteRouteUseCase
import com.sdu.izdesdu.domain.usecase.GetRouteByIdUseCase
import com.sdu.izdesdu.domain.usecase.GetUserProgressUseCase
import com.sdu.izdesdu.domain.usecase.SaveAnswerProgressUseCase
import com.sdu.izdesdu.domain.usecase.UpdateRouteStepUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionViewModelTest {

    private val sampleRoute = Route(
        id = "gate_to_canteen",
        title = "Путь до столовой",
        fromPlaceId = "main_gate",
        toPlaceId = "canteen",
        fromTitle = "Главный вход",
        toTitle = "Столовая",
        checkpoints = listOf(
            CheckPoint(
                id = 1,
                title = "Вход C",
                instruction = "От ворот ко входу C",
                mapX = 10f,
                mapY = 20f,
                question = Question(
                    text = "В каком году основан SDU?",
                    options = listOf("1991", "1996", "2001", "2010"),
                    correctIndex = 1,
                    hint = "Середина 90-х"
                )
            ),
            CheckPoint(
                id = 2,
                title = "Столовая",
                instruction = "Зайди в столовую",
                mapX = 30f,
                mapY = 40f,
                question = Question(
                    text = "Какой блок последний?",
                    options = listOf("A", "C", "I", "K"),
                    correctIndex = 2,
                    hint = "После H"
                )
            )
        )
    )

    private fun createProgressRepo() = ProgressRepositoryImpl(ProgressLocalDataSource())

    private fun createViewModel(
        routeId: String = "gate_to_canteen",
        step: Int = 1,
        progressRepo: ProgressRepositoryImpl = createProgressRepo()
    ): QuestionViewModel {
        val routeRepo = object : RouteRepository {
            override fun getRoutes(): List<Route> = listOf(sampleRoute)
            override fun getRouteById(routeId: String): Route? =
                listOf(sampleRoute).find { it.id == routeId }
        }
        return QuestionViewModel(
            routeId = routeId,
            step = step,
            getRouteByIdUseCase = GetRouteByIdUseCase(routeRepo),
            checkAnswerUseCase = CheckAnswerUseCase(),
            getUserProgressUseCase = GetUserProgressUseCase(progressRepo),
            saveAnswerProgressUseCase = SaveAnswerProgressUseCase(progressRepo),
            updateRouteStepUseCase = UpdateRouteStepUseCase(progressRepo),
            completeRouteUseCase = CompleteRouteUseCase(progressRepo),
            dispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun init_loadsCheckpointAndInitialState() {
        val vm = createViewModel(step = 1)
        val state = vm.uiState.value

        assertFalse(state.isMissing)
        assertFalse(state.isLastStep)
        assertEquals(1, state.checkpointNumber)
        assertEquals(2, state.totalCheckpoints)
        assertEquals("Вход C", state.checkpoint?.title)
        assertNull(state.selectedIndex)
        assertNull(state.isCorrect)
        assertFalse(state.showHint)
        assertEquals(0, state.totalScore)
    }

    @Test
    fun init_withInvalidStepOrRoute_marksMissing() {
        assertTrue(createViewModel(routeId = "unknown", step = 1).uiState.value.isMissing)
        assertTrue(createViewModel(step = 0).uiState.value.isMissing)
        assertTrue(createViewModel(step = 9).uiState.value.isMissing)
    }

    @Test
    fun selectWrongThenRightAnswer_showsHintAndPreventsDoubleScore() {
        val progressRepo = createProgressRepo()
        val vm = createViewModel(step = 1, progressRepo = progressRepo)

        vm.selectAnswer(0)
        assertEquals(0, vm.uiState.value.selectedIndex)
        assertEquals(false, vm.uiState.value.isCorrect)
        assertTrue(vm.uiState.value.showHint)
        assertEquals(0, vm.uiState.value.totalScore)

        vm.selectAnswer(1)
        assertEquals(1, vm.uiState.value.selectedIndex)
        assertEquals(true, vm.uiState.value.isCorrect)
        assertEquals(10, vm.uiState.value.totalScore)
        assertEquals(10, vm.uiState.value.sessionEarnedScore)
        assertEquals(2, progressRepo.progress.value.routeSteps["gate_to_canteen"])

        // Selecting again after correct answer must not add duplicate points
        vm.selectAnswer(1)
        assertEquals(10, vm.uiState.value.totalScore)
        assertEquals(10, vm.uiState.value.sessionEarnedScore)
    }

    @Test
    fun showHint_revealsHintExplicitly() {
        val vm = createViewModel(step = 1)
        vm.showHint()
        assertTrue(vm.uiState.value.showHint)
    }

    @Test
    fun finishCurrentStep_onLastStep_marksRouteCompleted() {
        val progressRepo = createProgressRepo()
        val vm = createViewModel(step = 2, progressRepo = progressRepo)

        assertTrue(vm.uiState.value.isLastStep)
        val finished = vm.finishCurrentStep()

        assertTrue(finished)
        assertTrue(vm.uiState.value.isRouteFinished)
        assertTrue("gate_to_canteen" in progressRepo.progress.value.completedRouteIds)
    }
}
