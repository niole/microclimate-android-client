package com.example.microclimates

import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.textfield.TextInputLayout

class LinkSensorDialog : DialogFragment() {
    companion object {
        fun newInstance(device: BluetoothDevice): DialogFragment {
            return LinkSensorDialog().apply {
                arguments = Bundle().apply {
                    putParcelable("device", device)
                }
            }
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val device = arguments?.getParcelable<BluetoothDevice>("device")!!

            val builder = AlertDialog.Builder(it)
            val dialogView = layoutInflater.inflate(R.layout.link_sensor_dialog_view, null)
            builder.setView(dialogView)

            val submit = dialogView.findViewById<Button>(R.id.submit)
            val cancel = dialogView.findViewById<Button>(R.id.cancel)
            submit.setOnClickListener {
                val ssid = dialogView
                    .findViewById<TextInputLayout>(R.id.ssid)
                    .editText?.text
                    .toString()
                val password = dialogView
                    .findViewById<TextInputLayout>(R.id.password)
                    .editText?.text
                    .toString()

                val result = Bundle().apply {
                    putParcelable("device", device)
                    putParcelable("wifiSpecs", WifiSpecs(ssid, password))
                }

                setFragmentResult("sensorSetupRequest", result)
                dialog?.hide()
            }

            cancel.setOnClickListener {
                val result = Bundle().apply {
                    putParcelable("device", device)
                }
                setFragmentResult("sensorSetupRequest", result)
                dialog?.cancel()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}