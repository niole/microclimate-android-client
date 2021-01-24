package com.example.microclimates

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class SetupPageActivity : AppCompatActivity() {
    private val LOG_TAG = "SetupPageActivity"
    val REQUEST_ENABLE_BT = 1
    private val bluetoothEvents: BluetoothEventListener = BluetoothEventListener()
    private val peripheralSetupClient = BluetoothPeripheralSetupClient()
    private val viewModel: SetupPageViewModel by viewModels()

    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var parentLayout: View
    private var peripheralsListAdapter: PeripheralListViewAdapter? = null
    private val devices: ArrayList<DeviceViewModel> = arrayListOf()
    private var isBluetoothEventsRegistered = false
    private lateinit var deploymentId: String
    private lateinit var ownerId: String
    private lateinit var peripheralId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_page)

        deploymentId = intent.getStringExtra("deploymentId")!!
        ownerId = intent.getStringExtra("ownerId")!!
        peripheralId = intent.getStringExtra("peripheralId")!!

        parentLayout = layoutInflater.inflate(R.layout.activity_setup_page,null)

        supportFragmentManager
            .setFragmentResultListener("sensorSetupRequest", this) { _, bundle ->
                val device = bundle.getParcelable<BluetoothDevice>("device")!!
                val wifiSpecs = bundle.getParcelable<WifiSpecs>("wifiSpecs")
                handleDeviceSetup(device, wifiSpecs)
            }

        requestLocationPermissions {
            bluetoothAdapter = setupBluetoothAdapter()
            viewModel.setBluetoothEnabled(bluetoothAdapter.isEnabled)
        }

        peripheralsListAdapter = setupPeripheralList()

        setupBluetoothButtons(parentLayout, bluetoothAdapter)

        // TODO might have just broken bluetooth enabling
        viewModel.getBluetoothEnabled().observe({ lifecycle }){
            if (it) {
                setupBluetoothButtons(parentLayout, bluetoothAdapter)
            } else {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }

        viewModel.getDevices().observe({ lifecycle }) { createdDevices ->
            devices.clear()
            devices.addAll(createdDevices.map { it.value })
            peripheralsListAdapter?.notifyDataSetChanged()
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
        bluetoothEvents.setOnDiscoveryStartedHandler {
            getDiscoverButton()?.icon = resources.getDrawable(R.drawable.round_clear_black_24dp)

        }
        bluetoothEvents.setOnDiscoveryStoppecHandler {
            getDiscoverButton()?.icon = resources.getDrawable(R.drawable.round_add_black_24dp)
        }
        bluetoothEvents.setOnDeviceBondStateChanged { device -> viewModel.updateDevice(device) }
    }

    override fun onPause() {
        super.onPause()
        if (isBluetoothEventsRegistered) {
            this.unregisterReceiver(bluetoothEvents)
            isBluetoothEventsRegistered  = false
        }
    }

    override fun onResume() {
        super.onResume()
        requestLocationPermissions {
            registerBluetoothEvents()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK) {
                    getDiscoverButton()?.isEnabled = true
                } else {
                    Toast.makeText(
                        this,
                        "You must enable bluetooth in order to setup sensors.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            else -> Log.w(LOG_TAG,"Request with code $requestCode not recognized")
        }
    }

    private fun requestLocationPermissions(block: () -> Unit): Unit {
        val applicationContext: Context = getApplicationContext()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PermissionChecker.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_ADMIN
                ) == PermissionChecker.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_ADMIN), 1
                )
            }
            if (ContextCompat.checkSelfPermission(
                    this,
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
    }

    private fun setupBluetoothButtons(view: View, bluetoothAdapter: BluetoothAdapter): Unit {
        Log.d(LOG_TAG, "Setting up bluetooth buttons")

        val discoveryButton = getDiscoverButton()

        discoveryButton?.isEnabled = bluetoothAdapter.isEnabled

        if (bluetoothAdapter.isDiscovering) {
            discoveryButton?.icon = resources.getDrawable(R.drawable.round_clear_black_24dp)
        } else {
            discoveryButton?.icon = resources.getDrawable(R.drawable.round_add_black_24dp)
        }

        discoveryButton?.setOnClickListener(View.OnClickListener {
            if (!bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.startDiscovery()
            } else {
                bluetoothAdapter.cancelDiscovery()
            }
        })
    }

    private fun getDiscoverButton(): ExtendedFloatingActionButton? {
        return findViewById(R.id.discovery_button)
    }

    private fun setupPeripheralList(): PeripheralListViewAdapter {
        val listAdapter = PeripheralListViewAdapter(supportFragmentManager, lifecycle, devices)

        val listView = findViewById<ViewPager2>(R.id.peripherals_list)
        listView.adapter = listAdapter

        return listAdapter
    }

    private fun setupBluetoothAdapter(): BluetoothAdapter {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            throw Exception("This device doesn't support Bluetooth. Crash")
        }
        return adapter
    }

    fun registerBluetoothEvents(): Unit {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)

        this.registerReceiver(bluetoothEvents, filter)
        isBluetoothEventsRegistered  = true
    }

    private fun handleDeviceSetup(device: BluetoothDevice, wifiSpecs: WifiSpecs?): Unit {
        device.createBond()

        Thread(Runnable {
            peripheralSetupClient.setupDevice(
                wifiSpecs,
                peripheralId,
                deploymentId,
                device!!, { hid ->
                    Log.i(LOG_TAG, "Paired with $peripheralId with hardware $hid")
                    runOnUiThread {
                        Toast.makeText(
                            baseContext,
                            "Finished linking device with sensor",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    finish()
                }, {
                    Log.w(LOG_TAG, "Failed to paired with $peripheralId with anything")
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Failed to link this device with this peripheral. Try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }).start()
    }

}