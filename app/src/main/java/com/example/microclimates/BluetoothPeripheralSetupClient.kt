package com.example.microclimates

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.view.View
import android.widget.Toast
import java.util.*

class BluetoothPeripheralSetupClient(view: View) {
    private val view: View = view

    private val serviceUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var connectedSocket: BluetoothSocket? = null

    fun setupDevice(device: BluetoothDevice) {
        val host = getHost()
        val message = Json.encodeToString(PeripheralConfiguration(host))
        sendMessage(message, device)
    }

    fun cancelSetup() {
        if (connectedSocket != null) {
            println("Closing connected socket upon request.")
            connectedSocket?.close()
        } else {
            println("No socket exists to close.")
        }
    }

    private fun getHost(): String {
        return "ec2-35-161-83-246.us-west-2.compute.amazonaws.com" // TODO get from a db
    }

    private fun sendMessage(message: String, device: BluetoothDevice): Unit {
        // TODO post message to main activity for toasting
        view.post {
            Toast.makeText(view.context, "Sending setup data to ${device.name}", Toast.LENGTH_LONG)
        }

        println("Sending setup data: ${message}, to device: ${device}")

        try {
            connectedSocket = device.createRfcommSocketToServiceRecord(serviceUuid)

            connectedSocket?.connect()

            println("Connected to socket for ${device.name}")

            val outputStream = connectedSocket?.outputStream

            println("Writing to socket for ${device.name}")

            outputStream?.write(message.toByteArray())

            println("Wrote to socket for ${device.name}")

        } catch (error: Exception) {
            println("Something happened when sending a message to ${device.name}: $error")
        } finally {
            connectedSocket?.close()
            connectedSocket = null
        }

    }
}
