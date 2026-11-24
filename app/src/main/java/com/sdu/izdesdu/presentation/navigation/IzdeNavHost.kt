package com.sdu.izdesdu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sdu.izdesdu.domain.usecase.GetRoutesUseCase
import com.sdu.izdesdu.presentation.map.MapScreen
import com.sdu.izdesdu.presentation.place.PlaceScreen
import com.sdu.izdesdu.presentation.question.QuestionScreen
import com.sdu.izdesdu.presentation.route.RouteScreen
import com.sdu.izdesdu.presentation.weather.WeatherScreen
import com.sdu.izdesdu.presentation.weather.WeatherViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IzdeNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val getRoutes = koinInject<GetRoutesUseCase>()
    val weatherViewModel = koinViewModel<WeatherViewModel>()
    val startRoute: (String) -> Unit = { placeId ->
        val route = getRoutes().firstOrNull {
            it.fromPlaceId == "main_gate" && it.toPlaceId == placeId
        }
        if (route != null) {
            navController.navigate(RouteDetailRoute(route.id))
        }
    }

    NavHost(
        navController = navController,
        startDestination = MapRoute,
        modifier = modifier
    ) {
        composable<MapRoute> {
            MapScreen(
                onStartRoute = startRoute,
                onOpenPlace = { placeId ->
                    navController.navigate(PlaceDetailRoute(placeId))
                },
                onOpenWeather = {
                    navController.navigate(WeatherRoute) {
                        launchSingleTop = true
                    }
                },
                weatherViewModel = weatherViewModel
            )
        }
        composable<WeatherRoute> {
            WeatherScreen(
                onBack = { navController.navigateUp() },
                viewModel = weatherViewModel
            )
        }
        composable<PlaceDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<PlaceDetailRoute>()
            PlaceScreen(
                placeId = args.placeId,
                onBack = { navController.popBackStack() },
                onStartRoute = startRoute
            )
        }
        composable<RouteDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<RouteDetailRoute>()
            RouteScreen(
                routeId = args.routeId,
                onBack = { navController.popBackStack() },
                onStartStep = { step ->
                    navController.navigate(QuestionRoute(args.routeId, step))
                }
            )
        }
        composable<QuestionRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<QuestionRoute>()
            QuestionScreen(
                routeId = args.routeId,
                step = args.step,
                onBack = { navController.popBackStack() },
                onNextStep = { nextStep ->
                    navController.navigate(QuestionRoute(args.routeId, nextStep)) {
                        popUpTo<QuestionRoute> { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onFinish = {
                    navController.navigate(MapRoute) {
                        popUpTo<MapRoute> { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
