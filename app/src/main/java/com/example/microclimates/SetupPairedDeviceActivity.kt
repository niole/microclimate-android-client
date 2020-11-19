package com.example.microclimates

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doOnTextChanged

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
    lateinit var hardwareId: String
    lateinit var deploymentId: String
    lateinit var ownerId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_paired_device)

        hardwareId = intent.getStringExtra("hardwareId")
        deploymentId = intent.getStringExtra("deploymentId")
        ownerId = intent.getStringExtra("ownerId")

        findViewById<EditText>(R.id.device_name_input).validate("letters and numbers only") {
            it != ""
        }

        //val request = PeripheralOuterClass.NewPeripheral
        //    .newBuilder()
        //    .setDeploymentId(deploymentId)
        //    .setHardwareId(hardwareId)
        //    .setName()
        //    .setOwnerUserId(ownerId)
        //    .setType()
        //    .setUnit()
        //    .build()
        //Stubs.peripheralStub().createPeripheral(request)

    }
}