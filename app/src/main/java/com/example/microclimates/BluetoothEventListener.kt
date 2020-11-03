package com.example.microclimates

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BluetoothEventListener : BroadcastReceiver() {

    var onFound: (BluetoothDevice) -> Unit = {}

    var onBonded: (BluetoothDevice) -> Unit = {}

    var onDiscoveryStarted: () -> Unit = {}

    var onDeviceBondStateChangedHandler: (BluetoothDevice) -> Unit = {}

    var onDiscoveryStopped: () -> Unit = {}

    fun setOnFoundHandler(value: (BluetoothDevice) -> Unit) {
        onFound = value
    }

    fun setOnBondedHandler(value: (BluetoothDevice) -> Unit) {
        onBonded = value
    }

    fun setOnDiscoveryStoppecHandler(value: () -> Unit): Unit {
        onDiscoveryStopped = value
    }

    fun setOnDiscoveryStartedHandler(value: () -> Unit): Unit {
        onDiscoveryStarted = value
    }

    fun setOnDeviceBondStateChanged(value: (BluetoothDevice) -> Unit): Unit {
        onDeviceBondStateChangedHandler = value
    }

    override fun onReceive(context: Context?, nullableIntent: Intent?) {
        if (nullableIntent != null) {
            val intent = nullableIntent!!
            val action: String = intent.action

            when(action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    println("Action discovery started received")
                    onDiscoveryStarted()
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    println("Action discovery finished received")
                    onDiscoveryStopped()
                }

                BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                    println("Action pairing request received")
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val currentBondState = device.getBondState()

                    onDeviceBondStateChangedHandler(device)

                    val bondingStatus = when(currentBondState) {
                        BluetoothDevice.BOND_BONDED -> "BOND_BONDED"
                        BluetoothDevice.BOND_BONDING -> "BOND_BONDING"
                        BluetoothDevice.BOND_NONE -> "BOND_NONE"
                        else -> "didn't account for this bonding state $currentBondState"
                    }

                    println("Bonding state for ${device.name} is $bondingStatus")

                    if (currentBondState == BluetoothDevice.BOND_BONDED) {
                        onBonded(device)
                    }
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address

                    //if (deviceName == SetupPage.SENSOR_DEVICE_NAME) { // TODO this won't always be so straightforward
                        // how do we differentiate between devices
                        println("found sensor device: deviceName $deviceName deviceHardwareAddress $deviceHardwareAddress")
                        Toast.makeText(context, "Found device $deviceName", Toast.LENGTH_SHORT).show()
                        onFound(device)
                   // }
                }
                else -> println("Not watching for action $action")
            }
        }
    }
}