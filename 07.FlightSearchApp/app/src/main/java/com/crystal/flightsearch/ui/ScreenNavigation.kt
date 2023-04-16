package com.crystal.flightsearch.ui

import android.inputmethodservice.Keyboard
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crystal.flightsearch.R
import com.crystal.flightsearch.data.Airport
import java.text.SimpleDateFormat
import java.util.Locale


enum class BusScheduleScreens {
    FullSchedule,
    RouteSchedule
}

private const val TAG = "FlightApp"

@Composable
fun FlightSearchApp(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory)
) {
    val navController = rememberNavController()
    val airportList by viewModel.homeUiState.collectAsState()

    val homeScreenTitle = stringResource(R.string.airport_screen_title)
    var topAppBarTitle by remember { mutableStateOf(homeScreenTitle) }

    val onBackHandler = {
        topAppBarTitle = homeScreenTitle
        navController.navigateUp()
    }

    var currentInput: String by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            FlightTopAppBar(
                title = topAppBarTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = { onBackHandler() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = modifier.padding(innerPadding),
            startDestination = BusScheduleScreens.FullSchedule.name
        ) {
            composable(BusScheduleScreens.FullSchedule.name) {



                Column() {


                    TextField(value = currentInput, onValueChange = { currentInput = it })


                    if (currentInput == "") {
                        Log.d(TAG, "fulll list size: ${airportList.itemList.size}")

                        Text("Full")
                        AirportList(airportList = airportList.itemList,
                            onScheduleClick = { airportCode ->
                                Log.d(TAG, "$it")
                                navController.navigate(
                                    "${BusScheduleScreens.RouteSchedule.name}/$airportCode"
                                )
                        })
                    }
                    else {
                        val matchedAirport by viewModel.getAirportName("%$currentInput%")
                            .collectAsState(emptyList())
                        Log.d(TAG, "matched list size: ${matchedAirport.size}")
                        Text("Matched")

                        AirportList(airportList = matchedAirport, onScheduleClick = {
                            Log.d(TAG, "$it")
                        })
                    }
                }




//                FullScheduleScreen(
//                    busSchedules = fullSchedule.itemList,
//                    onScheduleClick = { busStopName ->
//                        navController.navigate(
//                            "${BusScheduleScreens.RouteSchedule.name}/$busStopName"
//                        )
//                        topAppBarTitle = busStopName
//                    }
//                )
            }
            val busRouteArgument = "busRoute"
            composable(
                route = BusScheduleScreens.RouteSchedule.name + "/{$busRouteArgument}",
                arguments = listOf(navArgument(busRouteArgument) { type = NavType.StringType })
            ) { backStackEntry ->
                val airportCode = backStackEntry.arguments?.getString(busRouteArgument)
                    ?: error("busRouteArgument cannot be null")
                val routeSchedule by viewModel.getFavoriteCode(airportCode).collectAsState(emptyList())
                Text("FavoriteScreen")

                               RouteScheduleScreen(
                    airportCode = airportCode,
                    busSchedules = routeSchedule,
                    onBack = { onBackHandler() }
                )
            }
        }
    }
}



@Composable
fun AirportList(
    airportList: List<Airport>,
    modifier: Modifier = Modifier,
    airportCode: String? = null,
    onScheduleClick: ((String) -> Unit)? = null,
) {
    val airportNameText = if (airportCode == null) {
        stringResource(R.string.airport_name)
    } else {
        "$airportCode"
//        "$airportName ${stringResource(R.string.route_stop_name)}"
    }

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(airportNameText)
            Text("arrival time??")
        }
        Divider()
        FavoriteScreen(
            airportList = airportList,
            onScheduleClick = onScheduleClick
        )
    }
}


@Composable
fun FavoriteScreen(
    airportList: List<Airport>,
    modifier: Modifier = Modifier,
    onScheduleClick: ((String) -> Unit)? = null
) {

    LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(
            items = airportList,
            key = { busSchedule -> busSchedule.id }
        ) { schedule ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = onScheduleClick != null) {
                        onScheduleClick?.invoke(schedule.name)
                    }
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (onScheduleClick == null) {
                    Text(
                        text = "--",
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight(300)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = schedule.name,
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight(300)
                        )
                    )
                }
                Text(
                    text = schedule.code,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600)
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(2f)
                )

//                Text(
//                    text = schedule.passengers,
//                    style = MaterialTheme.typography.body1.copy(
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight(600)
//                    ),
//                    textAlign = TextAlign.End,
//                    modifier = Modifier.weight(2f)
//                )
            }
        }
    }
}



@Composable
fun FlightTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (canNavigateBack) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(
                            com.crystal.flightsearch.R.string.back
                        )
                    )
                }
            },
            modifier = modifier
        )
    } else {
        TopAppBar(
            title = { Text(title) },
            modifier = modifier
        )
    }
}