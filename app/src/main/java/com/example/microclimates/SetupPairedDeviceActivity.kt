package com.example.microclimates

import api.PeripheralOuterClass.NewPeripheral.PeripheralType
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import api.PeripheralOuterClass
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_setup_paired_device.*
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

fun EditText.validate(message: String, validator: (String) -> Boolean) {
    this.doOnTextChanged { text, _, _, _ ->
        if (validator(text.toString())) {
            this.error = null
        } else {
            this.error = message
        }
    }
    this.error = if (validator(this.text.toString())) null else message
}

class SetupPairedDeviceActivity : Activity() {
    private val LOG_TAG = "SetupPairedDeviceActivity"
    private val peripheralNameValidator = Regex("^[0-9a-zA-Z]+\$", RegexOption.DOT_MATCHES_ALL)
    private val noTypeSelected = "No type selected"
    private val thermalType = "Thermal"
    private val particleType = "Particle"
    private val peripheralTypes = listOf<String>(noTypeSelected, thermalType, particleType)
    lateinit var peripheralSetupClient: BluetoothPeripheralSetupClient

    lateinit var deploymentId: String
    lateinit var ownerId: String
    lateinit var device: BluetoothDevice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_paired_device)

        peripheralSetupClient = BluetoothPeripheralSetupClient()

        device = intent.getParcelableExtra("device")
        deploymentId = intent.getStringExtra("deploymentId")
        ownerId = intent.getStringExtra("ownerId")

        val spinner = peripheral_type_selector
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, peripheralTypes)
            spinner.adapter = adapter
        }

        findViewById<EditText>(R.id.device_name_input).validate("letters and numbers only") {
            peripheralNameValidator.matches(it)
        }

        submit_new_peripheral.setOnClickListener {
            validateAll { peripheralName, peripheralType ->
                Toast.makeText(baseContext, "Creating new peripheral", Toast.LENGTH_SHORT).show()

                val unit = when(peripheralType) {
                    PeripheralType.PARTICLE -> "PM2.5"
                    PeripheralType.THERMAL -> "F"
                    else -> {
                        Log.e(LOG_TAG, "peripheral type ${peripheralType} is not recognized. Defaulting to farenheit")
                        "F"
                    }
                }

                try {
                    Thread(Runnable {
                        val newPeripheral = createPeripheral(peripheralName, peripheralType, unit)

                        peripheralSetupClient.setupDevice(
                            newPeripheral.id,
                            deploymentId,
                            device, { hid ->
                                Log.i(LOG_TAG, "Paired with ${newPeripheral.id} with hardware $hid")
                                Toast.makeText(baseContext, "Finished creating peripheral", Toast.LENGTH_LONG).show()
                                finish()
                        }, {
                            Log.w(LOG_TAG, "Failed to paired with ${newPeripheral.id} with anything")
                        })
                    }).start()


                } catch (e: RuntimeException) {
                    Log.e(LOG_TAG, "Failed to create initial peripheral $peripheralName. Can't continue pairing process", e)
                    Toast.makeText(baseContext, "Something went wrong. Couldn't create this sensor", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun createPeripheral(peripheralName: String, peripheralType: PeripheralType, unit: String): PeripheralOuterClass.Peripheral {
        val request = PeripheralOuterClass.NewPeripheral
            .newBuilder()
            .setDeploymentId(deploymentId)
            .setHardwareId(
                PeripheralOuterClass
                    .NullableString
                    .newBuilder()
                    .setNull(com.google.protobuf.NullValue.NULL_VALUE)
            )
            .setName(peripheralName)
            .setOwnerUserId(ownerId)
            .setType(peripheralType)
            .setUnit(unit)
            .build()

            // create peripheral
            val perphChannel = Channels.peripheralChannel()
            val stub = Stubs.peripheralStub(perphChannel)
            val newPeripheral = stub.createPeripheral(request)
            perphChannel.shutdownNow()
            perphChannel.awaitTermination(1, TimeUnit.SECONDS)

            return newPeripheral
    }

    private fun validateAll(onSuccess: (String, PeripheralType) -> Unit): Unit {
        clearFindViewByIdCache()

        val name: String = device_name_input.text.toString()
        val pType: String = peripheral_type_selector.selectedItem.toString()
        var pTypeEnum = PeripheralType.forNumber(0)

        var allValid = true
        if (device_name_input.error != null) {
            allValid = false
            Toast.makeText(baseContext, "Please fix name", Toast.LENGTH_SHORT).show()
        }

        if (pType == noTypeSelected) {
            allValid = false
            Toast.makeText(baseContext, "Please select a peripheral type", Toast.LENGTH_SHORT).show()
        } else {
            pTypeEnum = PeripheralType.forNumber(when(pType) {
                thermalType -> 0
                particleType  -> 1
                else -> 0
            })
        }

        if (allValid) {
            onSuccess(name, pTypeEnum)
        }
    }
}