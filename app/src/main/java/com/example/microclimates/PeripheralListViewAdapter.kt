package com.example.microclimates

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class PeripheralListViewAdapter(
    private val pageViewModel: SetupPageViewModel,
    activity: Activity,
    private val resourceId: Int,
    private val handleDeviceSetup: (BluetoothDevice) -> Unit
) : ArrayAdapter<DeviceViewModel?>(activity, resourceId) {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewModel = getItem(position)!!
        var buttons = convertView
        try {
            if (buttons == null) {
                buttons = inflater?.inflate(resourceId, null)!!
            }

            val device = viewModel.device
            buttons.id = viewModel.id
            buttons.findViewById<TextView>(R.id.device_name).text = if (device.name == null) "unnamed" else device.name
            buttons.findViewById<TextView>(R.id.pair_status).text = when(viewModel.bondStatus) {
                BondStatus.PAIRING -> "connecting..."
                BondStatus.NOT_PAIRED -> "not connected"
                BondStatus.PAIRED -> "connected"
            }

            buttons.findViewById<Button>(R.id.pair_button).setOnClickListener {
                handleDeviceSetup(device)
            }
            buttons.findViewById<Button>(R.id.remove_button).setOnClickListener {
                pageViewModel.removeDevice(viewModel)
            }

        } catch (e: java.lang.Exception) {
            Log.e("Failure", e.toString())
        }
        return buttons!!
    }

    companion object {
        private var inflater: LayoutInflater? = null
    }

    init {
        try {
            inflater = activity.layoutInflater
        } catch (e: java.lang.Exception) {
            Log.e("Failure", "couldn't get inflater service")
        }
    }
}
