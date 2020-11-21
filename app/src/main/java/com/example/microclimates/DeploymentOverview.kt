package com.example.microclimates

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import api.PeripheralOuterClass
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import kotlinx.android.synthetic.main.fragment_peripheral.view.*
import java.util.concurrent.TimeUnit

class DeploymentOverview : Fragment() {

    private val LOG_TAG = "DeploymentOverview"
    private var listAdapter: ArrayAdapter<PeripheralOuterClass.Peripheral>? = null
    private val coreViewModel: CoreStateViewModel by activityViewModels()

    companion object {
        fun newInstance(): Fragment = DeploymentOverview()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parentLayout = inflater.inflate(R.layout.deployment_overview_fragment, container, false)
        var resourceId: Int = R.layout.fragment_peripheral

        val adapterPeripherals = mutableListOf<PeripheralOuterClass.Peripheral>()

        listAdapter = object : ArrayAdapter<PeripheralOuterClass.Peripheral>(activity, resourceId, adapterPeripherals) {
            override fun getCount(): Int {
                val total = adapterPeripherals?.size
                if (total != null) {
                    return total
                }
                return 0
            }
            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                var baseView = convertView
                if (baseView == null) {
                   baseView = inflater?.inflate(resourceId, null)!!
                }

                val peripheral = getItem(position)
                baseView.peripheral_name.text = peripheral.name
                baseView.peripheral_type.text = peripheral.type.toString()
                baseView.last_received_event_time.text = "unknown"
                baseView.remove_peripheral_button.setOnClickListener {
                    // TODO confirmation modal
                    Log.i(LOG_TAG, "Removing peripheral $peripheral")
                    val perphChannel = Channels.peripheralChannel()
                    try {
                        val stub = Stubs.peripheralStub(perphChannel)
                        stub.removePeripheral(peripheral)
                        coreViewModel.removePeripheral(peripheral.id)
                    } catch (error: Throwable) {
                        Log.e(LOG_TAG, "Failed to remove peripheral $peripheral. message: ${error.message}, cause: ${error.cause}")
                    } finally {
                        perphChannel.shutdown()
                        perphChannel.awaitTermination(5, TimeUnit.SECONDS)
                    }

                }
                return baseView
            }
        }
        val listView = parentLayout.findViewById(R.id.peripherals) as ListView
        listView.adapter = listAdapter

        return parentLayout
    }

    override fun onResume() {
        super.onResume()
        val existingDeployment = coreViewModel.getDeployment().value
        if (existingDeployment != null) {
            coreViewModel.refetchDeploymentPeripherals(existingDeployment.id)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        coreViewModel.getOwner().observeForever { user ->
            if (user != null) {
                view?.findViewById<TextView>(R.id.user_name)?.text = user.name
            }
        }

        coreViewModel.getDeployment().observeForever { deployment ->
            if (deployment != null) {
                view?.findViewById<TextView>(R.id.deployment_name)?.text = deployment.name
                coreViewModel.refetchDeploymentPeripherals(deployment.id)
            }
        }

        coreViewModel.getPeripherals().observeForever {
            listAdapter?.clear()
            listAdapter?.addAll(it)
        }
    }
}