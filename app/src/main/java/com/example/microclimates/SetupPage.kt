package com.example.microclimates

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class SetupPage : Fragment() {
    val REQUEST_ENABLE_BT = 1
    private val SETUP_PAGE_TAG = "SetupPage"
    val bluetoothEvents: BluetoothEventListener = BluetoothEventListener()
    lateinit var peripheralSetupClient: BluetoothPeripheralSetupClient
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var parentLayout: View
    lateinit var viewModel: SetupPageViewModel
    lateinit var peripheralsListAdapter: PeripheralListViewAdapter
    var devices: List<DeviceViewModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentLayout = inflater.inflate(R.layout.fragment_setup_page, container, false)
        viewModel = setupPageViewModel()
        peripheralSetupClient = BluetoothPeripheralSetupClient(
            parentLayout.findViewById(R.id.peripheral_setup_page),
            viewModel
        )
        peripheralsListAdapter = PeripheralListViewAdapter(
            viewModel,
            peripheralSetupClient,
            activity!!,
            R.layout.pair_management_buttons,
            devices
        )
        val listView = parentLayout.findViewById(R.id.peripherals_list) as ListView
        listView.adapter = peripheralsListAdapter

        setupBluetoothButtons(parentLayout)
        bluetoothEvents.setOnFoundHandler { device ->
            // TODO should use something that's meant for this instead of my hand made
            // callbacks
            viewModel.addDevice(device)
            val bonding = device.createBond() // TODO should we call this here
            Log.i(SETUP_PAGE_TAG,"Bonding status with ${device.name}: $bonding")
        }
        bluetoothEvents.setOnBondedHandler { device ->
            viewModel.updateDevice(device)
            Toast.makeText(
                context,
                "Paired with ${device.name}. Proceed by pressing the ${device.name} setup button.",
                Toast.LENGTH_LONG
            )
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
                nonNullActivity.unregisterReceiver(bluetoothEvents)
            },
            "Activity did not exist when attempting to unregister a bluetooth event receiver in setup page"
        )
    }

    override fun onResume() {
        super.onResume()
        requestLocationPermissions {
            setupBluetoothAdapter()
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

            else -> Log.w(SETUP_PAGE_TAG,"Request with code $requestCode not recognized")
        }
    }

    fun requestLocationPermissions(block: () -> Unit): Unit {
        Toast.makeText(context, "Checking bluetooth and location permissions", Toast.LENGTH_LONG).show()

        return doOrWarnIfActivityNotExists({ nonNullActivity ->
            val applicationContext: Context = context!!

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

    fun setupBluetoothButtons(view: View): Unit {
        Log.d(SETUP_PAGE_TAG, "Setting up bluetooth buttons")
        getDiscoverButton(view)?.setOnClickListener(View.OnClickListener {
            bluetoothAdapter.startDiscovery()
        })
    }

    private fun getDiscoverButton(view: View?): Button? {
        if (view != null) {
            return view?.findViewById<Button>(R.id.discovery_button)
        } else {
            Log.d(SETUP_PAGE_TAG,"Couldn't find the discovery button. Returning nothing.")
        }
        return null
    }

    private fun setupBluetoothAdapter(): Unit {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            throw Exception("This device doesn't support Bluetooth. Crash")
        } else {
            bluetoothAdapter = adapter
            viewModel.setBluetoothEnabled(adapter.isEnabled)
        }
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
            Log.w(SETUP_PAGE_TAG,warning)

        }
    }

    private fun setupPageViewModel(): SetupPageViewModel {
        val pageViewModel = ViewModelProvider(
            viewModelStore,
            SetupPageViewModelFactory()
        ).get(SetupPageViewModel::class.java)

        pageViewModel.getBluetoothEnabled().observe(this, Observer<Boolean?> {
            if (it == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                onActivityResult(REQUEST_ENABLE_BT, -1, null)
            }
        })

        pageViewModel.getDevices().observe(this, {
            val devices = it
            if (devices != null) {
                Log.d(SETUP_PAGE_TAG,"Device change")
                peripheralsListAdapter.clear()
                peripheralsListAdapter.addAll(devices.map { it.value })
            }
        })

        return pageViewModel
    }
}