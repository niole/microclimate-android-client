package com.example.microclimates

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BluetoothEventListener : BroadcastReceiver() {

    var onFound: (BluetoothDevice) -> Unit = {}

    var onBonded: (BluetoothDevice) -> Unit = {}

    fun setOnFoundHandler(value: (BluetoothDevice) -> Unit) {
        onFound = value
    }

    fun setOnBondedHandler(value: (BluetoothDevice) -> Unit) {
        onBonded = value
    }

    override fun onReceive(context: Context?, nullableIntent: Intent?) {
        if (nullableIntent != null) {
            val intent = nullableIntent!!
            val action: String = intent.action

            when(action) {
                BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                    println("PAIRING REQUEST")
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val currentBondState = device.getBondState()

                    val bondingStatus = when(currentBondState) {
                        BluetoothDevice.BOND_BONDED -> "BOND_BONDED"
                        BluetoothDevice.BOND_BONDING -> "BOND_BONDING"
                        BluetoothDevice.BOND_NONE -> "BOND_NONE"
                        else -> "didn't account for this bonding state $currentBondState"
                    }

                    println("ACTION_BOND_STATE_CHANGED: $currentBondState A.K.A. $bondingStatus")

                    if (currentBondState == BluetoothDevice.BOND_BONDED) {
                        onBonded(device)
                    }
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address

                    if (deviceName == MainActivity.SENSOR_DEVICE_NAME) { // TODO this won't always be so straightforward
                        // how do we differentiate between devices
                        println("found sensor device: deviceName $deviceName deviceHardwareAddress $deviceHardwareAddress")
                        Toast.makeText(context, "Found device $deviceName", Toast.LENGTH_SHORT).show()
                        onFound(device)
                    }
                }
                else -> println("Not watching for action $action")
            }
        }
    }
}