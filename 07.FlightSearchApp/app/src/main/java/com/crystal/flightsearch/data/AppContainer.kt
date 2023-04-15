package com.crystal.flightsearch.data

import android.content.Context

interface AppContainer {
    val flightRepository: FlightRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineFlightRepository]
 */

class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [FlightRepository]
     */
    override val flightRepository: FlightRepository by lazy {
        OfflineFlightRepository(
            FlightDatabase.getDatabase(context).airportDao(),
            FlightDatabase.getDatabase(context).favoriteDao(),
        )
    }
}