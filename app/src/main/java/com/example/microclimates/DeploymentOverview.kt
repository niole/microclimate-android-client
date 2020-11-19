package com.example.microclimates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import api.PeripheralOuterClass
import com.example.microclimates.api.Stubs

class DeploymentOverview : Fragment() {

    private val LOG_TAG = "DeploymentOverview"
    private var listAdapter: ArrayAdapter<PeripheralOuterClass.Peripheral>? = null

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
                baseView.findViewById<TextView>(R.id.peripheral_name).text = peripheral.name
                baseView.findViewById<TextView>(R.id.peripheral_type).text = peripheral.type.toString()
                baseView.findViewById<TextView>(R.id.last_received_event_time).text = "unknown"

                return baseView
            }
        }
        val listView = parentLayout.findViewById(R.id.peripherals) as ListView
        listView.adapter = listAdapter

        return parentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val coreViewModel: CoreStateViewModel by activityViewModels()

        coreViewModel.getOwner().observeForever { user ->
            if (user != null) {
                view?.findViewById<TextView>(R.id.user_name)?.text = user.name
            }
        }

        coreViewModel.getDeployment().observeForever { deployment ->
            if (deployment != null) {
                view?.findViewById<TextView>(R.id.deployment_name)?.text = deployment.name

                val peripherals = getPeripherals(deployment.id)
                coreViewModel.setPeripherals(peripherals)
            }
        }

        coreViewModel.getPeripherals().observeForever {
            listAdapter?.clear()
            listAdapter?.addAll(it)
        }
    }

    private fun getPeripherals(deploymentId: String): List<PeripheralOuterClass.Peripheral> {
        val request = PeripheralOuterClass.GetDeploymentPeripheralsRequest
            .newBuilder()
            .setDeploymentId(deploymentId)
            .build()
         return Stubs.peripheralStub().getDeploymentPeripherals(request).asSequence().toList()
    }

}