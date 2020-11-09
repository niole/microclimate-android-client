package com.example.microclimates

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothEventListener : BroadcastReceiver() {
    private val LOG_TAG = "BluetoothEventListener"

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

                    Log.i(LOG_TAG,"Bonding state for $device is $bondingStatus")

                    if (currentBondState == BluetoothDevice.BOND_BONDED) {
                        onBonded(device)
                    }
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (isPeripheral(device)) {
                        onFound(device)
                    }
                }
                else -> Log.d(LOG_TAG,"Not watching for action $action")
            }
        }
    }

   private fun isPeripheral(device: BluetoothDevice): Boolean {
       // TODO implement
       // peripherals device names are their hardwareids
       return true
   }
}