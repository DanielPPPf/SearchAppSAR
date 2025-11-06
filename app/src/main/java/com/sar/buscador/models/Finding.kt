package com.sar.buscador.models

import java.util.Date

data class Finding(
    val id: String,
    val type: FindingType,
    val location: Location,
    val confidence: ConfidenceLevel,
    val notes: String? = null,
    val timestamp: Date = Date()
)

enum class FindingType {
    WITNESS,      // Alta confianza
    OBJECT,       // Media confianza
    TRACE         // Baja confianza
}

enum class ConfidenceLevel(val weight: Double) {
    HIGH(1.0),
    MEDIUM(0.6),
    LOW(0.3)
}

// Extension function para obtener confianza según tipo
fun FindingType.getConfidenceLevel(): ConfidenceLevel {
    return when (this) {
        FindingType.WITNESS -> ConfidenceLevel.HIGH
        FindingType.OBJECT -> ConfidenceLevel.MEDIUM
        FindingType.TRACE -> ConfidenceLevel.LOW
    }
}