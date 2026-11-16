package com.sdu.izdesdu.di

import android.content.Context
import com.sdu.izdesdu.data.local.PlaceLocalDataSource
import com.sdu.izdesdu.data.local.ProgressLocalDataSource
import com.sdu.izdesdu.data.local.RouteLocalDataSource
import com.sdu.izdesdu.data.repository.PlaceRepositoryImpl
import com.sdu.izdesdu.data.repository.ProgressRepositoryImpl
import com.sdu.izdesdu.data.repository.RouteRepositoryImpl
import com.sdu.izdesdu.domain.repository.PlaceRepository
import com.sdu.izdesdu.domain.repository.ProgressRepository
import com.sdu.izdesdu.domain.repository.RouteRepository
import com.sdu.izdesdu.domain.usecase.CheckAnswerUseCase
import com.sdu.izdesdu.domain.usecase.CompleteRouteUseCase
import com.sdu.izdesdu.domain.usecase.GetPlaceByIdUseCase
import com.sdu.izdesdu.domain.usecase.GetPlacesUseCase
import com.sdu.izdesdu.domain.usecase.GetRouteByIdUseCase
import com.sdu.izdesdu.domain.usecase.GetRoutesUseCase
import com.sdu.izdesdu.domain.usecase.GetUserProgressUseCase
import com.sdu.izdesdu.domain.usecase.SubmitRouteAnswerUseCase

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    private val placeLocalDataSource = PlaceLocalDataSource(appContext)
    private val routeLocalDataSource = RouteLocalDataSource(appContext)
    private val progressLocalDataSource = ProgressLocalDataSource()

    val placeRepository: PlaceRepository = PlaceRepositoryImpl(placeLocalDataSource)
    val routeRepository: RouteRepository = RouteRepositoryImpl(routeLocalDataSource)
    val progressRepository: ProgressRepository = ProgressRepositoryImpl(progressLocalDataSource)

    val getPlacesUseCase = GetPlacesUseCase(placeRepository)
    val getPlaceByIdUseCase = GetPlaceByIdUseCase(placeRepository)
    val getRoutesUseCase = GetRoutesUseCase(routeRepository)
    val getRouteByIdUseCase = GetRouteByIdUseCase(routeRepository)
    val checkAnswerUseCase = CheckAnswerUseCase()
    val getUserProgressUseCase = GetUserProgressUseCase(progressRepository)
    val submitRouteAnswerUseCase = SubmitRouteAnswerUseCase(routeRepository, progressRepository, checkAnswerUseCase)
    val completeRouteUseCase = CompleteRouteUseCase(progressRepository, routeRepository)
}
