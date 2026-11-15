package com.sdu.izdesdu.data.local

import com.sdu.izdesdu.data.repository.RouteRepositoryImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RouteLocalDataSourceTest {
    private val source = RouteLocalDataSource { id: Int -> "resource_$id" }

    @Test
    fun everyMapDestinationHasRouteWithExpectedCheckpointCount() {
        val counts = mapOf("canteen" to 5, "library" to 3, "medical" to 5, "block_i" to 6,
            "halls" to 4, "dorm" to 3, "gym" to 4)
        val routes = source.loadRoutes()
        assertEquals(counts.keys, routes.map { it.toPlaceId }.toSet())
        assertEquals(routes.size, routes.map { it.id }.distinct().size)
        routes.forEach { route ->
            assertEquals("gate_to_${route.toPlaceId}", route.id)
            assertEquals("main_gate", route.fromPlaceId)
            assertEquals(counts.getValue(route.toPlaceId), route.checkpoints.size)
            assertEquals(route.checkpoints.size, route.checkpoints.map { it.id }.distinct().size)
        }
    }

    @Test
    fun allCheckpointsContainValidQuestionsAndCoordinates() {
        source.loadRoutes().flatMap { it.checkpoints }.forEach { checkpoint ->
            assertTrue(checkpoint.title.isNotBlank())
            assertTrue(checkpoint.instruction.isNotBlank())
            assertTrue(checkpoint.mapX.isFinite() && checkpoint.mapY.isFinite())
            val question = checkpoint.question
            assertTrue(question.text.isNotBlank())
            assertTrue(question.hint.isNotBlank())
            assertEquals(4, question.options.size)
            assertEquals(4, question.options.distinct().size)
            assertTrue(question.correctIndex in question.options.indices)
        }
    }

    @Test
    fun repositoryReturnsRequestedRouteAndHandlesMissingId() {
        val repository = RouteRepositoryImpl(source)
        source.loadRoutes().forEach { assertEquals(it, repository.getRouteById(it.id)) }
        assertNull(repository.getRouteById("missing"))
    }
}
