package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.data.local.ProgressLocalDataSource
import com.sdu.izdesdu.data.repository.ProgressRepositoryImpl
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.domain.model.Question
import com.sdu.izdesdu.domain.model.Route
import com.sdu.izdesdu.domain.repository.RouteRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.Executors

class RouteProgressTest {
    private val question = Question("Question", listOf("Right", "Wrong", "Third", "Fourth"), 0, "Hint")
    private val route = Route(
        "route", "Route", "gate", "library", "Gate", "Library",
        listOf(
            CheckPoint(10, "First", "Go", 1f, 1f, question),
            CheckPoint(20, "Last", "Finish", 2f, 2f, question)
        )
    )
    private val routes = object : RouteRepository {
        override fun getRoutes() = listOf(route, route.copy(id = "other"), route.copy(id = "empty", checkpoints = emptyList()))
        override fun getRouteById(routeId: String) = getRoutes().find { it.id == routeId }
    }
    private val progress = ProgressRepositoryImpl(ProgressLocalDataSource())
    private val submit = SubmitRouteAnswerUseCase(routes, progress, CheckAnswerUseCase())
    private val complete = CompleteRouteUseCase(progress, routes)

    @Test
    fun futureCheckpointCannotBeAnsweredOrCompleted() {
        assertNull(submit("route", 2, 0))
        assertFalse(complete("route"))
        assertEquals(0, progress.progress.value.totalScore)
        assertTrue(progress.progress.value.routeProgress.isEmpty())
    }

    @Test
    fun wrongAttemptsAndRewardAreStoredPerCheckpoint() {
        submit("route", 1, 1)
        submit("route", 1, 2)
        submit("route", 1, 0)
        submit("route", 1, 0)
        submit("route", 1, 1)
        val state = progress.progress.value
        assertEquals(10, state.totalScore)
        assertEquals(3, state.answeredCount)
        assertEquals(1, state.correctCount)
        assertEquals(2, state.routeProgress.getValue("route").mistakes)
        assertEquals(1, state.routeProgress.getValue("route").completedCheckpoints)
    }

    @Test
    fun repeatedSubmissionsFromConcurrentCallersAwardOnce() {
        val executor = Executors.newFixedThreadPool(4)
        try {
            val futures = (1..20).map { executor.submit { submit("route", 1, 0) } }
            futures.forEach { it.get() }
            assertEquals(10, progress.progress.value.totalScore)
            assertEquals(1, progress.progress.value.answeredCount)
        } finally {
            executor.shutdownNow()
        }
    }

    @Test
    fun onlyFullyAnsweredRoutesCanFinishAndFinishingIsIdempotent() {
        submit("route", 1, 0)
        assertFalse(complete("route"))
        submit("route", 2, 1)
        assertFalse(complete("route"))
        submit("route", 2, 0)
        assertTrue(complete("route"))
        assertTrue(complete("route"))
        submit("route", 2, 0)
        assertEquals(setOf("route"), progress.progress.value.completedRouteIds)
        assertEquals(20, progress.progress.value.totalScore)
    }

    @Test
    fun routesHaveIndependentRewardsAndMistakeCounts() {
        submit("route", 1, 1)
        submit("route", 1, 0)
        submit("other", 1, 0)
        val state = progress.progress.value
        assertEquals(20, state.totalScore)
        assertEquals(10, state.routeProgress.getValue("route").earnedPoints)
        assertEquals(10, state.routeProgress.getValue("other").earnedPoints)
        assertEquals(1, state.routeProgress.getValue("route").mistakes)
        assertEquals(0, state.routeProgress.getValue("other").mistakes)
    }

    @Test
    fun invalidArgumentsDoNotMutateProgress() {
        for (id in listOf("unknown", "empty")) {
            assertNull(submit(id, 1, 0))
            assertFalse(complete(id))
        }
        for (step in listOf(Int.MIN_VALUE, -1, 0, 3, Int.MAX_VALUE)) assertNull(submit("route", step, 0))
        for (index in listOf(-1, 4, Int.MAX_VALUE)) assertNull(submit("route", 1, index))
        assertEquals(0, progress.progress.value.answeredCount)
    }

    @Test
    fun revisitingAnEarlierCheckpointDoesNotMoveProgressBack() {
        submit("route", 1, 0)
        submit("route", 2, 0)
        progress.setRouteStep("route", 1)
        assertEquals(2, progress.progress.value.routeSteps["route"])
    }

    @Test
    fun invalidCorrectIndexCannotAwardPoints() {
        val result = CheckAnswerUseCase()(question.copy(correctIndex = -1), -1)
        assertFalse(result.isCorrect)
        assertEquals(0, result.earnedPoints)
    }
}
