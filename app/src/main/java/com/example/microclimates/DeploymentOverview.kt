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
import androidx.fragment.app.setFragmentResultListener
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("onPeripheralRemoveSubmit") { _, bundle ->
            val peripheralId = bundle.getString("peripheralId")
            Log.i(LOG_TAG, "Removing peripheral with id $peripheralId")
            val perphChannel = Channels.peripheralChannel()
            val peripheral = coreViewModel.getPeripheralById(peripheralId)
            if (peripheral != null) {
                try {
                    val stub = Stubs.peripheralStub(perphChannel)
                    stub.removePeripheral(peripheral)
                    coreViewModel.removePeripheral(peripheral.id)
                } catch (error: Throwable) {
                    Log.e(LOG_TAG, "Failed to remove peripheral with id $peripheralId. message: ${error.message}, cause: ${error.cause}")
                } finally {
                    perphChannel.shutdown()
                    perphChannel.awaitTermination(5, TimeUnit.SECONDS)
                }

            } else {
                Log.w(LOG_TAG, "Couldn't remove peripheral with id $peripheralId, because didn't exist in view model.")
            }
        }
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
                    val confirmModal = ConfirmRemovePeripheralDialog().apply {
                        arguments = Bundle().apply {
                            putString("peripheralId", peripheral.id)
                            putString("peripheralName", peripheral.name)
                        }
                    }
                    confirmModal.show(parentFragmentManager, "ConfirmRemovePeripheralDialog")
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