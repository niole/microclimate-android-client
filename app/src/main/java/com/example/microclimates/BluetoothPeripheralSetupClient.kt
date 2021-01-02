package com.example.microclimates

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.microclimates.api.Channels
import java.util.*

class BluetoothPeripheralSetupClient() {
    private val LOG_TAG = "BluetoothPeripheralSetupClient"
    private val serviceUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var connectedSocket: BluetoothSocket? = null

    // TODO get rid of function callbacks
    fun setupDevice(
        peripheralId: String,
        deploymentId: String,
        device: BluetoothDevice,
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit
    ) {
        val hardwareId = UUID.randomUUID().toString()
        val message = Json.encodeToString(
            PeripheralConfiguration(
                peripheralServiceDomain=Channels.peripheralServiceDomain.url(),
                eventServiceDomain=Channels.eventServiceDomain.url(),
                hardwareId = hardwareId,
                peripheralId = peripheralId,
                deploymentId = deploymentId
            )
        )
        sendMessage(message, device, {
            onSuccess(hardwareId)
        }, {
            onFailure()
        })
    }

    private fun sendMessage(
        message: String,
        device: BluetoothDevice,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ): Unit {
        Log.d(LOG_TAG,"Sending setup data: ${message}, to device: ${device}")

        try {
            connectedSocket = device.createRfcommSocketToServiceRecord(serviceUuid)

            connectedSocket?.connect()

            Log.d(LOG_TAG,"Connected to socket for ${device.name}")

            val outputStream = connectedSocket?.outputStream

            Log.d(LOG_TAG,"Writing to socket for ${device.name}")

            outputStream?.write(message.toByteArray())

            Log.d(LOG_TAG,"Wrote to socket for ${device}")

            onSuccess()
        } catch (error: Exception) {
            Log.e(LOG_TAG,"Something happened when sending a message to ${device.name}: $error")
            onFailure()

        } finally {
            connectedSocket?.close()
            connectedSocket = null
        }

    }
}
