package com.example.microclimates

import kotlinx.serialization.*

@Serializable
data class PeripheralConfiguration(
    val domain: String,
    val peripheralId: String,
    val deploymentId: String
)
