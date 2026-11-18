package com.sdu.izdesdu.di

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
import com.sdu.izdesdu.presentation.map.MapViewModel
import com.sdu.izdesdu.presentation.place.PlaceViewModel
import com.sdu.izdesdu.presentation.question.QuestionViewModel
import com.sdu.izdesdu.presentation.route.RouteViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    single { PlaceLocalDataSource(androidContext()) }
    single { RouteLocalDataSource(androidContext()) }
    single { ProgressLocalDataSource() }

    single<PlaceRepository> { PlaceRepositoryImpl(get()) }
    single<RouteRepository> { RouteRepositoryImpl(get()) }
    single<ProgressRepository> { ProgressRepositoryImpl(get()) }

    single { GetPlacesUseCase(get()) }
    single { GetPlaceByIdUseCase(get()) }
    single { GetRoutesUseCase(get()) }
    single { GetRouteByIdUseCase(get()) }
    single { CheckAnswerUseCase() }
    single { GetUserProgressUseCase(get()) }
    single { SubmitRouteAnswerUseCase(get(), get(), get()) }
    single { CompleteRouteUseCase(get(), get()) }

    viewModelOf(::MapViewModel)
    viewModel { (placeId: String) -> PlaceViewModel(placeId, get()) }
    viewModel { (routeId: String) -> RouteViewModel(routeId, get(), get()) }
    viewModel { (routeId: String, step: Int) ->
        QuestionViewModel(
            routeId = routeId,
            step = step,
            getRouteByIdUseCase = get(),
            getUserProgressUseCase = get(),
            submitRouteAnswerUseCase = get(),
            completeRouteUseCase = get()
        )
    }
}
