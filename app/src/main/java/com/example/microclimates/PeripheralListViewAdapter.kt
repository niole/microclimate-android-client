package com.example.microclimates

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class PeripheralListViewAdapter(
    val pageViewModel: SetupPageViewModel,
    val peripheralSetupClient: BluetoothPeripheralSetupClient,
    activity: Activity,
    val resourceId: Int,
    val peripherals: List<DeviceViewModel>?
) : ArrayAdapter<DeviceViewModel?>(activity, resourceId, peripherals) {

    override fun getCount(): Int {
        val total = peripherals?.size
        if (total != null) {
            return total
        }
        return 0
    }

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
            buttons.findViewById<TextView>(R.id.device_name).text = device.address
            buttons.findViewById<TextView>(R.id.pair_status).text = when(viewModel.bondStatus) {
                BondStatus.PAIRING -> "connecting..."
                BondStatus.NOT_PAIRED -> "not connected"
                BondStatus.PAIRED -> "connected"
            }

            buttons.findViewById<Button>(R.id.pair_button).setOnClickListener {
                Thread(Runnable { peripheralSetupClient.setupDevice(device) }).start()
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
            inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } catch (e: java.lang.Exception) {
            Log.e("Failure", "couldn't get inflater service")
        }
    }
}
