package com.example.microclimates

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.*
import api.Events
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.*
import java.util.concurrent.TimeUnit

class DeploymentOverview : Fragment() {

    private lateinit var stubs: Stubs
    private val LOG_TAG = "DeploymentOverview"
    private var listAdapter: ArrayAdapter<LivePeripheralModel>? = null
    private val coreViewModel: CoreStateViewModel by activityViewModels()
    private val model : DeploymentOverviewViewModel by viewModels()

    companion object {
        fun newInstance(): Fragment = DeploymentOverview()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stubs = Stubs(requireContext())
        setFragmentResultListener("onPeripheralRemoveSubmit") { _, bundle ->
            val peripheralId = bundle.getString("peripheralId")!!
            Log.i(LOG_TAG, "Removing peripheral with id $peripheralId")
            val perphChannel = Channels.getInstance(requireContext()).peripheralChannel()
            val peripheral = coreViewModel.getPeripheralById(peripheralId)
            if (peripheral != null) {
                try {
                    val stub = stubs.peripheralStub(perphChannel)
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

        coreViewModel.getPeripherals().observe({ lifecycle }) { peripherals ->
            val deployment = coreViewModel.getDeployment().value
            if (deployment != null) {
                Thread {
                    val eventsChannel = Channels.getInstance(requireContext()).eventsChannel()
                    val stub = stubs.eventsStub(eventsChannel)
                    val request = Events.MostRecentEventsForDeploymentRequest.newBuilder().setDeploymentId(deployment.id).build()
                    val events = stub.mostRecentDeploymentEvents(request).asSequence()
                    val eventsMap = mutableMapOf<String, Events.MeasurementEvent>()
                    events.forEach {
                        eventsMap += Pair(it.peripheralId, it)
                    }

                    val newPeripherals = peripherals.map { p ->
                        val lastEvent = eventsMap[p.id]
                        LivePeripheralModel(
                            id = p.id,
                            name = p.name,
                            type = p.type.toString(),
                            lastEvent = if (lastEvent != null) Date(lastEvent.timeStamp.seconds * 1000) else null,
                            lastReading = if (lastEvent != null) "${lastEvent.value} ${p.unit}" else null
                        )
                    }

                    view?.post {
                        model.setNewConnectedPeripherals(newPeripherals)
                    }

                    eventsChannel.shutdown()
                    eventsChannel.awaitTermination(5, TimeUnit.SECONDS)
                }.start()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parentLayout = inflater.inflate(R.layout.deployment_overview_fragment, container, false)
        var resourceId: Int = R.layout.fragment_peripheral

        val adapterPeripherals = mutableListOf<LivePeripheralModel>()

        listAdapter = object : ArrayAdapter<LivePeripheralModel>(requireContext(), resourceId, adapterPeripherals) {
            override fun getCount(): Int {
                return adapterPeripherals.size
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
                baseView.findViewById<TextView>(R.id.peripheral_name).text = peripheral?.name
                baseView.findViewById<TextView>(R.id.peripheral_type).text = peripheral?.type.toString()
                baseView.findViewById<TextView>(R.id.last_received_event).text = if (peripheral?.lastEvent != null && peripheral?.lastReading != null) {
                    "${peripheral.lastReading} on ${peripheral.lastEvent} "
                } else {
                    "No readings yet"
                }

                baseView.findViewById<Button>(R.id.link_hardware_button).setOnClickListener {
                    val deploymentId = coreViewModel.getDeployment().value?.id
                    val ownerId = coreViewModel.getOwner().value?.id
                    val peripheralId = peripheral?.id

                    if (deploymentId == null || ownerId == null || peripheralId == null) {
                        Log.e(
                            LOG_TAG,
                            "Can't start hardware setup page activity, some arguments are null: " +
                                    "peripheralId: $peripheralId, ownerId: $ownerId, deploymentId: $deploymentId"
                        )
                        Toast.makeText(
                            context,
                            "Something went wrong. Can't start linking process.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val setupPageArguments = Intent(context, SetupPageActivity::class.java).apply {
                            putExtra("peripheralId", peripheralId)
                            putExtra("deploymentId", deploymentId)
                            putExtra("ownerId", ownerId)
                        }
                        startActivity(setupPageArguments)
                    }
                }
                baseView.findViewById<Button>(R.id.edit_peripheral_button).setOnClickListener {
                    val deploymentId = coreViewModel.getDeployment().value?.id
                    val ownerId = coreViewModel.getOwner().value?.id
                    val peripheral = coreViewModel.getPeripherals().value?.find { p -> p.id == peripheral?.id }

                    if (peripheral != null && deploymentId != null && ownerId != null) {

                        CreateEditPeripheralDialog
                            .newInstance(
                                ownerId,
                                deploymentId,
                                peripheral.id,
                                peripheral.type,
                                peripheral.name
                            )
                            .show(childFragmentManager, "CreateEditPeripheralDialog")

                    } else {
                        Log.e(
                            LOG_TAG,
                            "Missing data, can't open peripheral edit modal peripheral"+
                                " $peripheral, ownerId $ownerId, deploymentId $deploymentId"
                        )
                        Toast.makeText(context, "Something went wrong. Can't open dialog", Toast.LENGTH_SHORT).show()
                    }
                }

                baseView.findViewById<Button>(R.id.remove_peripheral_button).setOnClickListener {
                    val confirmModal = ConfirmRemovePeripheralDialog().apply {
                        arguments = Bundle().apply {
                            putString("peripheralId", peripheral?.id)
                            putString("peripheralName", peripheral?.name)
                        }
                    }
                    confirmModal.show(parentFragmentManager, "ConfirmRemovePeripheralDialog")
                }
                return baseView
            }
        }
        val listView = parentLayout.findViewById(R.id.peripherals) as ListView
        listView.adapter = listAdapter

        model.getConnectedPeripherals().observe({ lifecycle }) {
            listAdapter?.clear()
            listAdapter?.addAll(it)
        }

        coreViewModel.getOwner().observe({ lifecycle }) { user ->
            if (user != null) {
                view?.findViewById<TextView>(R.id.user_name)?.text = user.name
            }
        }

        coreViewModel.getDeployment().observe({ lifecycle }) { deployment ->
            if (deployment != null) {
                view?.findViewById<TextView>(R.id.deployment_name)?.text = deployment.name
                coreViewModel.refetchDeploymentPeripherals(deployment.id, requireContext())
            }
        }

        parentLayout.findViewById<ExtendedFloatingActionButton>(R.id.create_peripheral_button).setOnClickListener {
            val ownerId = coreViewModel.getOwner().value?.id!!
            val deploymentId = coreViewModel.getDeployment().value?.id!!

            CreateEditPeripheralDialog
                .newInstance(ownerId, deploymentId, null, null, null)
                .show(childFragmentManager, "CreateEditPeripheralDialog")
        }

        return parentLayout
    }

    override fun onResume() {
        super.onResume()
        val existingDeployment = coreViewModel.getDeployment().value
        if (existingDeployment != null) {
            coreViewModel.refetchDeploymentPeripherals(existingDeployment.id, requireContext())
        }
    }

}