package com.crystal.flightsearch.ui

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crystal.flightsearch.R
import com.crystal.flightsearch.data.Airport
import com.crystal.flightsearch.data.Favorite
import com.crystal.flightsearch.ui.theme.FlightSearchTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RouteScheduleScreen(
    airportCode: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModel.factory)
) {

    val coroutineScope = rememberCoroutineScope()

    // val routeSchedule by viewModel.getFavoriteMatchedCode("%$airportCode%").collectAsState(emptyList())
    val routeSchedule by viewModel.getAllFavorites().collectAsState(emptyList())

    BackHandler { onBack() }

    Text(airportCode)

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(airportCode)
            Text("arrival time??")
        }
        Divider()
        FavoriteListBody(
            favoriteList = routeSchedule,
            onClickStar = {
            },
        )
    }

}


@Composable
fun FavoriteListBody(
    favoriteList: List<Favorite>,
    modifier: Modifier = Modifier,
    onClickStar: () -> Unit,
    isStarOn: Boolean = false
) {

    LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(
            items = favoriteList,
            key = { favorite -> favorite.id }
        ) { favorite ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = onClickStar != null) {
//                        onClickStar?.invoke(favorite.departureCode)
                    }
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = favorite.departureCode,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(300)
                    )
                )
                Text(
                    text = favorite.destinationCode,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600)
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(2f)
                )

                IconButton(onClick = {
                    Log.d("TAG", "isStarOn: $isStarOn  -> toggle ${!isStarOn}")
//                    onClickStar(favorite, !isStarOn)
                } ) {
                    Icon(
                        imageVector = if (isStarOn) Icons.Filled.Star else Icons.Filled.StarOutline,
                        contentDescription = stringResource(
                            R.string.star
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FavoriteListBodyPreview() {

    FlightSearchTheme {
        FavoriteListBody( favoriteList =
        listOf(Favorite(1, "fasdf", "asdfasf")),
         onClickStar = {  },
        )
    }
}
/*
@Composable
fun BusScheduleScreen(
//    busSchedules: List<BusSchedule>,
    modifier: Modifier = Modifier,
    airportCode: String? = null,
    onScheduleClick: ((String) -> Unit)? = null,
) {
    val stopNameText = if (airportCode == null) {
        stringResource(R.string.stop_name)
    } else {
        "$airportCode ${stringResource(R.string.route_stop_name)}"
    }

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stopNameText)
            Text(stringResource(R.string.destination_code))
        }
        Divider()
        BusScheduleDetails(
//            busSchedules = busSchedules,
            onScheduleClick = onScheduleClick
        )
    }
}

@Composable
fun BusScheduleDetails(
//    busSchedules: List<BusSchedule>,
    modifier: Modifier = Modifier,
    onScheduleClick: ((String) -> Unit)? = null
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(
            items = busSchedules,
            key = { busSchedule -> busSchedule.id }
        ) { schedule ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = onScheduleClick != null) {
                        onScheduleClick?.invoke(schedule.stopName)
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
                        text = schedule.stopName,
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight(300)
                        )
                    )
                }
                Text(
                    text = SimpleDateFormat("h:mm a", Locale.getDefault())
                        .format(schedule.arrivalTimeInMillis),
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600)
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}
*/