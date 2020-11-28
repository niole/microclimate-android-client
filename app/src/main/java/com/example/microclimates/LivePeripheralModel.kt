package com.example.microclimates

import java.util.*

data class LivePeripheralModel(
    val id: String,
    val name: String,
    val type: String,
    val lastEvent: Date?,
    val lastReading: String?
)
