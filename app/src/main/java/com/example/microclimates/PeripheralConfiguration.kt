package com.example.microclimates

import kotlinx.serialization.*

@Serializable
data class PeripheralConfiguration(
    val peripheralServiceDomain: String,
    val eventServiceDomain: String,
    val peripheralId: String,
    val deploymentId: String,
    val hardwareId: String
)
