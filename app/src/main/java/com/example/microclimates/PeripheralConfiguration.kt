package com.example.microclimates

import kotlinx.serialization.*

@Serializable
data class PeripheralConfiguration(val host: String, val hardwareId: String)
