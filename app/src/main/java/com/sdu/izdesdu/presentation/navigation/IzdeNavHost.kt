package com.sdu.izdesdu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sdu.izdesdu.di.AppContainer

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

        }
        composable<FromToRoute> {

        }
        composable<PlaceDetailRoute> { backStackEntry ->

        }
        composable<RouteDetailRoute> { backStackEntry ->

        }
        composable<QuestionRoute> { backStackEntry ->

        }
        composable<MyRoutesRoute> {

        }
        composable<ProfileRoute> {

        }
    }
}