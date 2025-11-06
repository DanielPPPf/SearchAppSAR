package com.sar.buscador.models

import java.util.Date

data class SearchCase(
    val id: String,
    val lastSeenLocation: Location,
    val timestamp: Date,
    val timeElapsed: TimeElapsed? = null,
    val mobilityType: MobilityType? = null,
    val findings: MutableList<Finding> = mutableListOf(),
    val status: SearchStatus = SearchStatus.ACTIVE,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val method: LocationMethod
)

enum class LocationMethod {
    CURRENT_GPS,
    MAP_SELECTION,
    MANUAL_INPUT
}

enum class TimeElapsed(val hours: Int) {
    ONE_HOUR(1),
    THREE_HOURS(3),
    SIX_HOURS(6),
    ONE_DAY(24),
    THREE_DAYS(72),
    ONE_WEEK(168)
}

enum class MobilityType {
    WALKING,
    CAR,
    BICYCLE,
    MOTORCYCLE
}

enum class SearchStatus {
    ACTIVE,
    COMPLETED,
    ARCHIVED
}