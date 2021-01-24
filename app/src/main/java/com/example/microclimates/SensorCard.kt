package com.example.microclimates

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener

class SensorCard : Fragment() {
    private val pageViewModel : SetupPageViewModel by activityViewModels()

    companion object {
        fun newInstance(deviceViewModel: DeviceViewModel): Fragment {
            return SensorCard().apply {
                arguments = Bundle().apply {
                    putParcelable("deviceViewModel", deviceViewModel)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener("sensorSetupRequest", this) { _, bundle ->
            setFragmentResult("sensorSetupRequest", bundle)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = arguments?.getParcelable<DeviceViewModel>("deviceViewModel")!!
        val device = viewModel.device
        val bondStatus = viewModel.bondStatus

        val buttons = layoutInflater.inflate(R.layout.pair_management_buttons, container, false)

        buttons.id = viewModel.id
        buttons.findViewById<TextView>(R.id.device_name).text = if (device.name == null) "unnamed" else device.name
        buttons.findViewById<TextView>(R.id.pair_status).text = when(bondStatus) {
            BondStatus.PAIRING -> "connecting..."
            BondStatus.NOT_PAIRED -> "not connected"
            BondStatus.PAIRED -> "connected"
        }

        buttons.findViewById<Button>(R.id.pair_button).setOnClickListener {
            LinkSensorDialog.newInstance(device).show(childFragmentManager, null)
        }

        buttons.findViewById<Button>(R.id.remove_button).setOnClickListener {
            pageViewModel.removeDevice(viewModel)
        }

        return buttons
    }

}