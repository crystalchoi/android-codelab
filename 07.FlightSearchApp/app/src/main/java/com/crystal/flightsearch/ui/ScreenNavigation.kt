package com.crystal.flightsearch.ui

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
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crystal.flightsearch.R
import com.crystal.flightsearch.data.Airport
import java.text.SimpleDateFormat
import java.util.Locale


//enum class BusScheduleScreens {
//    FullSchedule,
//    RouteSchedule
//}



sealed class NavRoutes(val route: String) {
    object Home: NavRoutes("home")
    object Favorite: NavRoutes("favorite")

}


private const val TAG = "FlightApp"

@Composable
fun FlightSearchApp(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory)
) {
    val navController = rememberNavController()
    val airportList by viewModel.homeUiState.collectAsState()
    val queryUiState by viewModel.queryUiState.collectAsState()

    var currentInput: String by remember { mutableStateOf(queryUiState.storedQuery) }

    val homeScreenTitle = stringResource(R.string.airport_screen_title)
    var topAppBarTitle by remember { mutableStateOf(homeScreenTitle) }

    val onBackHandler = {
        topAppBarTitle = homeScreenTitle
        navController.navigateUp()
    }



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
            startDestination = NavRoutes.Home.route
        ) {
            composable(NavRoutes.Home.route) {
                Column() {
                    CustomTextField(title = "which Airport?",
                        textState = currentInput,
                        onTextChange = { currentInput = it})

                    val matchedAirport by viewModel.getAirportName("%$currentInput%")
                        .collectAsState(emptyList())

                    Log.d(TAG, "list size: ${airportList.itemList.size} " +
                            "matched size: ${matchedAirport.size}")

                    AirportList(airportList = if (currentInput.isEmpty()) airportList.itemList
                                                   else matchedAirport,
                        onScheduleClick = { airportCode ->
                            Log.d(TAG, "$airportCode")

                            viewModel.saveStoredQuery(airportCode)
                            navController.navigate(
                                "${NavRoutes.Favorite.route}/$airportCode"
                            ) {
                                launchSingleTop = true
                                popUpTo(NavRoutes.Home.route)
                            }
                            topAppBarTitle = airportCode
                        })
                    }
                }

            val codeArgument = "code"
            composable(
                route = NavRoutes.Favorite.route + "/{$codeArgument}",
                arguments = listOf(navArgument(codeArgument) { type = NavType.StringType })
            ) { backStackEntry ->
                val airportCode = backStackEntry.arguments?.getString(codeArgument)
                    ?: error("busRouteArgument cannot be null")
                RouteScheduleScreen(
                    airportCode = airportCode,
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
        AirportListBody(
            airportList = airportList,
            onScheduleClick = onScheduleClick
        )
    }
}


@Composable
fun AirportListBody(
    airportList: List<Airport>,
    modifier: Modifier = Modifier,
    onScheduleClick: ((String) -> Unit)? = null
) {

    LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(
            items = airportList,
            key = { airport -> airport.id }
        ) { airport ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = onScheduleClick != null) {
                        onScheduleClick?.invoke(airport.code)
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
                        text = airport.name,
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight(300)
                        )
                    )
                }
                Text(
                    text = airport.code,
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
            elevation = 12.dp,
            modifier = modifier
        )
    } else {
        TopAppBar(
            title = { Text(title) },
            modifier = modifier,
        )
    }
}