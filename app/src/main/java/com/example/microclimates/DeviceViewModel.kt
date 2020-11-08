package com.example.microclimates

import android.bluetooth.BluetoothDevice

enum class BondStatus {
    PAIRING,
    NOT_PAIRED,
    PAIRED;
    companion object {
        fun fromDeviceBondStatus(status: Int): BondStatus {
            return when(status) {
                BluetoothDevice.BOND_BONDED -> BondStatus.PAIRED
                BluetoothDevice.BOND_BONDING -> BondStatus.PAIRING
                BluetoothDevice.BOND_NONE -> BondStatus.NOT_PAIRED
                else -> NOT_PAIRED
            }
        }

    }
}


data class DeviceViewModel(
    val id: Int,
    val bondStatus: BondStatus,
    val device: BluetoothDevice
)