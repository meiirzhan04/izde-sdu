package com.sdu.izdesdu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.presentation.map.MapScreen
import com.sdu.izdesdu.presentation.place.PlaceScreen
import com.sdu.izdesdu.presentation.question.QuestionScreen
import com.sdu.izdesdu.presentation.route.RouteScreen

@Composable
fun IzdeNavHost(
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
    navController: NavHostController= rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = MapRoute,
        modifier = modifier
    ) {
        composable<MapRoute> {
            MapScreen(
                onStartRoute = { placeId ->
                    navController.navigate(RouteDetailRoute("gate_to_$placeId"))
                },
                onOpenPlace = { placeId ->
                    navController.navigate(PlaceDetailRoute(placeId))
                }
            )
        }
        composable<FromToRoute> {

        }
        composable<PlaceDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<PlaceDetailRoute>()
            PlaceScreen(
                placeId = args.placeId,
                onBack = { navController.popBackStack() },
                onStartRoute = { placeId ->
                    navController.navigate(RouteDetailRoute("gate_to_$placeId"))
                }
            )
        }
        composable<RouteDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<RouteDetailRoute>()
            RouteScreen(
                routeId = args.routeId,
                appContainer = appContainer,
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
                appContainer = appContainer,
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
        composable<MyRoutesRoute> {

        }
        composable<ProfileRoute> {

        }
    }
}
