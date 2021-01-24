package com.example.microclimates

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class BondStatus {
    PAIRING,
    NOT_PAIRED,
    PAIRED;
    companion object {
        fun fromDeviceBondStatus(status: Int): BondStatus {
            return when(status) {
                BluetoothDevice.BOND_BONDED -> PAIRED
                BluetoothDevice.BOND_BONDING -> PAIRING
                BluetoothDevice.BOND_NONE -> NOT_PAIRED
                else -> NOT_PAIRED
            }
        }

    }
}


@Parcelize
class DeviceViewModel(
    val id: Int,
    val bondStatus: BondStatus,
    val device: BluetoothDevice
) : Parcelable