package com.example.busschedule.ui.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.busschedule.R
import com.example.busschedule.ui.*


@Composable
fun BusScheduleApp(
    modifier: Modifier = Modifier,
    viewModel: BusScheduleViewModel = viewModel(factory = BusScheduleViewModel.factory)
) {
    val navController = rememberNavController()
//    val fullSchedule by viewModel.homeUiState.collectAsState()
    val fullSchedule by viewModel.getFullSchedule().collectAsState(listOf())

    val fullScheduleTitle = stringResource(R.string.full_schedule)
    var topAppBarTitle by remember { mutableStateOf(fullScheduleTitle) }

    val onBackHandler = {
        topAppBarTitle = fullScheduleTitle
        navController.navigateUp()
    }

    Scaffold(
        topBar = {
            BusScheduleTopAppBar(
                title = topAppBarTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = { onBackHandler() }
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = modifier.padding(innerPadding),
            startDestination = BusScheduleScreens.FullSchedule.name
        ) {
            composable(BusScheduleScreens.FullSchedule.name) {
                FullScheduleScreen(
                    busSchedules = fullSchedule,
                    onScheduleClick = { busStopName ->
                        navController.navigate(
                            "${BusScheduleScreens.RouteSchedule.name}/$busStopName"
                        )
                        topAppBarTitle = busStopName
                    }
                )
            }
            val busRouteArgument = "busRoute"
            composable(
                route = BusScheduleScreens.RouteSchedule.name + "/{$busRouteArgument}",
                arguments = listOf(navArgument(busRouteArgument) { type = NavType.StringType })
            ) { backStackEntry ->
                val stopName = backStackEntry.arguments?.getString(busRouteArgument)
                    ?: error("busRouteArgument cannot be null")
                val routeSchedule by viewModel.getScheduleFor(stopName).collectAsState(emptyList())
                RouteScheduleScreen(
                    stopName = stopName,
                    busSchedules = routeSchedule,
                    onBack = { onBackHandler() }
                )
            }
        }
    }
}
