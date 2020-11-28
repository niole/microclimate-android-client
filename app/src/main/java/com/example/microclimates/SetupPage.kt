package com.example.microclimates

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders

class SetupPage : Fragment() {
    companion object {
        fun newInstance(): Fragment = SetupPage()
    }

    private val LOG_TAG = "SetupPage"
    val REQUEST_ENABLE_BT = 1
    val bluetoothEvents: BluetoothEventListener = BluetoothEventListener()
    lateinit var peripheralSetupClient: BluetoothPeripheralSetupClient
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var parentLayout: View
    lateinit var viewModel: SetupPageViewModel
    private var peripheralsListAdapter: PeripheralListViewAdapter? = null
    private var devices: List<DeviceViewModel> = mutableListOf()
    private var isBluetoothEventsRegistered = false
    private val parentViewModel: CoreStateViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = setupPageViewModel()
        requestLocationPermissions {
            bluetoothAdapter = setupBluetoothAdapter()
            viewModel.setBluetoothEnabled(bluetoothAdapter.isEnabled)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentLayout = inflater.inflate(R.layout.fragment_setup_page, container, false)
        peripheralSetupClient = BluetoothPeripheralSetupClient(
            parentLayout.findViewById(R.id.peripheral_setup_page),
            viewModel
        )
        peripheralsListAdapter = setupPeripheralList(viewModel)
        viewModel.getBluetoothEnabled().observe({ lifecycle }) {
            if (it) {
                setupBluetoothButtons(parentLayout, bluetoothAdapter)
            }
        }

        bluetoothEvents.setOnFoundHandler { device ->
            if (device.name != null) {
                viewModel.addDevice(device)
                Log.i(LOG_TAG,"Bonding status with ${device.name}: ${device.bondState}")
            } else {
                Log.i(LOG_TAG, "Found device ${device.address}, but it had no name")
            }
        }
        bluetoothEvents.setOnBondedHandler { device ->
            viewModel.updateDevice(device)
            Log.i(LOG_TAG, "Paired with ${device.name}.")
        }
        bluetoothEvents.setOnDiscoveryStartedHandler { getDiscoverButton(parentLayout)?.text = "x" }
        bluetoothEvents.setOnDiscoveryStoppecHandler { getDiscoverButton(parentLayout)?.text = "+" }
        bluetoothEvents.setOnDeviceBondStateChanged { device -> viewModel.updateDevice(device) }

        return parentLayout
    }

    override fun onPause() {
        super.onPause()
        doOrWarnIfActivityNotExists(
            { nonNullActivity ->
                if (isBluetoothEventsRegistered) {
                    nonNullActivity.unregisterReceiver(bluetoothEvents)
                    isBluetoothEventsRegistered  = false
                }
            },
            "Activity did not exist when attempting to unregister a bluetooth event receiver in setup page"
        )
    }

    override fun onResume() {
        super.onResume()
        requestLocationPermissions {
            registerBluetoothEvents()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK) {
                    getDiscoverButton(view)?.isEnabled = true
                } else {
                    Toast.makeText(
                        context,
                        "You must enable bluetooth in order to setup sensors.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            else -> Log.w(LOG_TAG,"Request with code $requestCode not recognized")
        }
    }

    private fun requestLocationPermissions(block: () -> Unit): Unit {
        return doOrWarnIfActivityNotExists({ nonNullActivity ->
            val applicationContext: Context = requireContext()

            if (ContextCompat.checkSelfPermission(
                    nonNullActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PermissionChecker.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    nonNullActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }

            if (ContextCompat.checkSelfPermission(
                    nonNullActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PermissionChecker.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        nonNullActivity,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) == PermissionChecker.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(
                        nonNullActivity,
                        arrayOf(Manifest.permission.BLUETOOTH_ADMIN), 1
                    )
                }
                if (ContextCompat.checkSelfPermission(
                        nonNullActivity,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    block()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "You must enable bluetooth and location permissions",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }, "Can't check for bluetooth permissions, activity doesn't exist")
    }

    private fun setupBluetoothButtons(view: View, bluetoothAdapter: BluetoothAdapter): Unit {
        Log.d(LOG_TAG, "Setting up bluetooth buttons")
        val discoveryButton = getDiscoverButton(view)
        discoveryButton?.text = if (bluetoothAdapter.isDiscovering) "x" else "+"
        discoveryButton?.setOnClickListener(View.OnClickListener {
            if (!bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.startDiscovery()
            } else {
                bluetoothAdapter.cancelDiscovery()
            }
        })
    }

    private fun getDiscoverButton(view: View?): Button? {
        if (view != null) {
            return view?.findViewById<Button>(R.id.discovery_button)
        } else {
            Log.d(LOG_TAG,"Couldn't find the discovery button. Returning nothing.")
        }
        return null
    }

    private fun setupPeripheralList(viewModel: SetupPageViewModel): PeripheralListViewAdapter {
        val listAdaper = PeripheralListViewAdapter(
            viewModel,
            requireActivity(),
            R.layout.pair_management_buttons,
            devices
        ) { device -> handleDeviceSetup(device) }
        val listView = parentLayout.findViewById(R.id.peripherals_list) as ListView
        listView.adapter = listAdaper
        return listAdaper
    }

    private fun setupBluetoothAdapter(): BluetoothAdapter {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            throw Exception("This device doesn't support Bluetooth. Crash")
        }
        return adapter
    }

    fun registerBluetoothEvents(): Unit {
        doOrWarnIfActivityNotExists({ nonNullActivity ->
            val filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            filter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)

            nonNullActivity.registerReceiver(bluetoothEvents, filter)
            isBluetoothEventsRegistered  = true
        }, "Activity doesn't exist, couldn't register bluetooth events")
    }

    fun doOrWarnIfActivityNotExists(block: (activity: Activity) -> Unit, warning: String): Unit {
        getOrWarn<Activity>(activity, block, warning)
    }

    fun <T>getOrWarn(o: T?, block: (o: T) -> Unit, warning: String? = null): Unit {
        val nonNullO = o
        if (nonNullO != null) {
           block(nonNullO)
        } else if (warning != null) {
            Log.w(LOG_TAG, warning)

        }
    }

    private fun setupPageViewModel(): SetupPageViewModel {
        val pageViewModel = ViewModelProviders.of(this).get(SetupPageViewModel::class.java)
        pageViewModel.getBluetoothEnabled().observe({ lifecycle }) { it ->
            if (it == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                onActivityResult(REQUEST_ENABLE_BT, -1, null)

            }
        }

        pageViewModel.getDevices().observe({ lifecycle }) { devices ->
            if (devices != null) {
                peripheralsListAdapter?.clear()
                peripheralsListAdapter?.addAll(devices.map { it.value })
            }
        }

        return pageViewModel
    }

    private fun handleDeviceSetup(device: BluetoothDevice): Unit {
        device.createBond()
        Thread(Runnable {
            peripheralSetupClient.setupDevice(
                parentViewModel.getDeployment().value?.id!!,
                device
            ) { hardwareId ->
                val deploymentId = parentViewModel.getDeployment().value?.id
                val ownerId = parentViewModel.getOwner().value?.id
                val intent = Intent(requireActivity(), SetupPairedDeviceActivity::class.java).apply {
                    putExtra("hardwareId", hardwareId)
                    putExtra("deploymentId", deploymentId)
                    putExtra("ownerId", ownerId)
                }

                if (deploymentId != null && ownerId != null) {
                    activity?.startActivity(intent)
                } else {
                    Log.e(LOG_TAG, "Deployment id and owner id don't exist. Cannot open setup paried device view.")
                    Toast.makeText(context, "Cannot finish setup process. Couldn't get all data in order to complete setup.", Toast.LENGTH_LONG).show()
                }
            }
        }).start()

    }
}