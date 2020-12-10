package com.example.microclimates

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import api.PeripheralOuterClass.NewPeripheral.PeripheralType
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import api.PeripheralOuterClass
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import java.util.concurrent.TimeUnit

class CreatePeripheralDialog : DialogFragment() {

    private val LOG_TAG = "CreatePeripheralDialog"
    private val noTypeSelected = "No type selected"
    private val thermalType = "Thermal"
    private val particleType = "Particle"
    private val peripheralNameValidator = Regex("^[0-9a-zA-Z]+\$", RegexOption.DOT_MATCHES_ALL)
    private val peripheralTypes = listOf<Pair<PeripheralType, String>>(
        Pair(PeripheralType.UNRECOGNIZED, noTypeSelected),
        Pair(PeripheralType.THERMAL, thermalType),
        Pair(PeripheralType.PARTICLE, particleType)
    )

    private val coreViewModel: CoreStateViewModel by activityViewModels()

    companion object {
        fun newInstance(ownerId: String, deploymentId: String): DialogFragment {
            return CreatePeripheralDialog().apply {
                arguments = Bundle().apply {
                    putString("deploymentId", deploymentId)
                    putString("ownerId", ownerId)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val deploymentId = arguments?.getString("deploymentId")!!
            val ownerId = arguments?.getString("ownerId")!!

            val builder = AlertDialog.Builder(it)

            val dialogView = layoutInflater.inflate(R.layout.peripheral_dialog_view_fragment, null)
            builder.setView(dialogView)

            val spinner = dialogView.findViewById<Spinner>(R.id.peripheral_type_selector)
            if (spinner != null) {
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    peripheralTypes.map { t -> t.second }
                )
                spinner.adapter = adapter
            }

            dialogView.findViewById<EditText>(R.id.device_name_input).validate(
                "Name can only contain alphanumeric characters"
            ) { v -> peripheralNameValidator.matches(v) }

            dialogView.findViewById<Button>(R.id.submit_new_peripheral).setOnClickListener {
                validateInputs(dialogView) { peripheralName, peripheralType, unit ->
                    val newPeripheral = createPeripheral(
                        deploymentId,
                        ownerId,
                        peripheralName,
                        peripheralType,
                        unit
                    )
                    coreViewModel.addPeripheral(newPeripheral)
                    dialog?.dismiss()
                }
            }

            dialogView.findViewById<Button>(R.id.cancel_new_peripheral).setOnClickListener {
                dialog?.cancel()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createPeripheral(
        deploymentId: String,
        ownerId: String,
        peripheralName: String,
        peripheralType: PeripheralOuterClass.NewPeripheral.PeripheralType,
        unit: String
    ): PeripheralOuterClass.Peripheral {
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

    private fun validateInputs(dView: View, callback: (String, PeripheralType, String) -> Unit): Unit {
        val nameEditText = dView.findViewById<EditText>(R.id.device_name_input)
        val newName = nameEditText.text.toString()
        val selectedIndex = dView.findViewById<Spinner>(R.id.peripheral_type_selector).selectedItemPosition
        val peripheralType = peripheralTypes[selectedIndex].first

        val isNameValid = nameEditText.error == null
        val isPTypeValid = peripheralType != PeripheralType.UNRECOGNIZED

        if (isNameValid && isPTypeValid) {
            val unit = when(peripheralType) {
                PeripheralType.PARTICLE -> "PM2.5"
                PeripheralType.THERMAL -> "F"
                else -> {
                    Log.e(LOG_TAG, "peripheral type ${peripheralType} is not recognized.")
                    "F"
                }
            }

            callback(newName, peripheralType, unit)
        } else {
            Log.w(LOG_TAG, "New peripheral inputs not valid. name $newName, peripheral type $peripheralType")

            if (peripheralType == PeripheralType.UNRECOGNIZED) {
                Toast.makeText(requireContext(), "Select a sensor type", Toast.LENGTH_SHORT).show()
            }
        }

    }

}

