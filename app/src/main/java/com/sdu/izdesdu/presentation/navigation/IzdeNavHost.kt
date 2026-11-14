package com.sdu.izdesdu.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sdu.izdesdu.di.AppContainer
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
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = { navController.navigate(RouteDetailRoute("gate_to_canteen")) }) {
                    Text("Открыть путь")
                }
            }
        }
        composable<FromToRoute> {

        }
        composable<PlaceDetailRoute> { backStackEntry ->

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
                    navController.navigate(QuestionRoute(args.routeId, nextStep))
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