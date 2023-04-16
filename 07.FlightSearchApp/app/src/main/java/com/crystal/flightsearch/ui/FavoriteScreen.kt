package com.crystal.flightsearch.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crystal.flightsearch.R
import com.crystal.flightsearch.data.Airport
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RouteScheduleScreen(
    airportCode: String,
//    busSchedules: List<Airport>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    BackHandler { onBack() }
    BusScheduleScreen(
//        busSchedules = busSchedules,
        modifier = modifier,
        airportCode = airportCode
    )
}


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
    }중
}
