package com.example.microclimates

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import android.view.View
import java.util.*

class BluetoothPeripheralSetupClient(val view: View, val setupPageViewModel: SetupPageViewModel) {
    private val LOG_TAG = "BluetoothPeripheralSetupClient"
    private val serviceUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var connectedSocket: BluetoothSocket? = null

    fun setupDevice(deploymentId: String, device: BluetoothDevice, onSuccess: (String) -> Unit) {
        val hardwareId = UUID.randomUUID().toString()

        val message = Json.encodeToString(
            PeripheralConfiguration(
                domain="192.168.1.162:6004",
                peripheralId = hardwareId,
                deploymentId = deploymentId
            )
        )
        sendMessage(message, device, {
            onSuccess(hardwareId)
        })
    }

    private fun sendMessage(message: String, device: BluetoothDevice, onSuccess: () -> Unit): Unit {
        view.post {
            setupPageViewModel.setPairing(device)
        }

        Log.d(LOG_TAG,"Sending setup data: ${message}, to device: ${device}")

        try {
            connectedSocket = device.createRfcommSocketToServiceRecord(serviceUuid)

            connectedSocket?.connect()

            Log.d(LOG_TAG,"Connected to socket for ${device.name}")

            val outputStream = connectedSocket?.outputStream

            Log.d(LOG_TAG,"Writing to socket for ${device.name}")

            outputStream?.write(message.toByteArray())

            Log.d(LOG_TAG,"Wrote to socket for ${device}")

            view.post {
                onSuccess()
            }
        } catch (error: Exception) {
            Log.e(LOG_TAG,"Something happened when sending a message to ${device.name}: $error")
            view.post {
                setupPageViewModel.setPairingFailed(device)
            }

        } finally {
            connectedSocket?.close()
            connectedSocket = null
        }

    }
}
