package com.example.microclimates

import android.bluetooth.BluetoothDevice

data class DeviceViewModel(
    val id: Int,
    val device: BluetoothDevice
)