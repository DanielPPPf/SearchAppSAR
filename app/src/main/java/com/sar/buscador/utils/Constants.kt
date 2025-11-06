package com.sar.buscador.utils

object Constants {
    // Configuración del mapa
    const val DEFAULT_MAP_ZOOM = 15f
    const val MAP_ANIMATION_DURATION = 300

    // Colores de zonas (en formato ARGB)
    const val ZONE_RED_COLOR = 0x80DC2626
    const val ZONE_ORANGE_COLOR = 0x80EA580C
    const val ZONE_YELLOW_COLOR = 0x80FACC15
    const val ZONE_GREEN_COLOR = 0x8016A34A

    // Radio de búsqueda base (en metros)
    const val BASE_SEARCH_RADIUS = 500

    // Multiplicadores según movilidad
    const val WALKING_MULTIPLIER = 1.0
    const val CAR_MULTIPLIER = 10.0
    const val BICYCLE_MULTIPLIER = 3.0
    const val MOTORCYCLE_MULTIPLIER = 8.0

    // Multiplicadores según tiempo
    const val TIME_MULTIPLIER_1H = 1.0
    const val TIME_MULTIPLIER_3H = 2.0
    const val TIME_MULTIPLIER_6H = 3.5
    const val TIME_MULTIPLIER_1D = 6.0
    const val TIME_MULTIPLIER_3D = 12.0
    const val TIME_MULTIPLIER_1W = 20.0

    // SharedPreferences
    const val PREFS_NAME = "SAR_Preferences"
    const val KEY_SEARCH_HISTORY = "search_history"

    // Request codes
    const val REQUEST_LOCATION_PERMISSION = 100
    const val REQUEST_ADD_FINDING = 101
}