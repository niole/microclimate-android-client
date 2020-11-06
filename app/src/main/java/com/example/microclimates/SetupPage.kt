package com.example.microclimates

import android.Manifest
import android.app.Activity
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class SetupPage : Fragment() {
    val REQUEST_ENABLE_BT = 1
    val bluetoothEvents: BluetoothEventListener = BluetoothEventListener()
    lateinit var peripheralSetupClient: BluetoothPeripheralSetupClient
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var parentLayout: View
    var foundDevices: Map<String, DeviceViewModel> = mutableMapOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentLayout = inflater.inflate(R.layout.fragment_setup_page, container, false)

        setupBluetoothButtons(parentLayout)
        bluetoothEvents.setOnFoundHandler { onFound(it) }
        bluetoothEvents.setOnBondedHandler { onBonded(it) }
        bluetoothEvents.setOnDiscoveryStartedHandler { getDiscoverButton(parentLayout)?.text = "x" }
        bluetoothEvents.setOnDiscoveryStoppecHandler { getDiscoverButton(parentLayout)?.text = "+" }
        bluetoothEvents.setOnDeviceBondStateChanged { device -> updateDeviceBondingState(device)}
        peripheralSetupClient = BluetoothPeripheralSetupClient(parentLayout.findViewById(R.id.peripheral_setup_page))

        return parentLayout
    }

    companion object {
        val SENSOR_DEVICE_NAME = "raspberrypi"
        fun newInstance() = SetupPage()
    }

    override fun onPause() {
        super.onPause()
        doOrWarnIfActivityNotExists({ nonNullActivity ->
            nonNullActivity.unregisterReceiver(bluetoothEvents)
        }, "Activity did not exist when attempting to unregister a bluetooth event receiver in setup page")
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
                    val bondedDevices = bluetoothAdapter.bondedDevices
                    val foundDevice = bondedDevices.find { it.name == SENSOR_DEVICE_NAME }
                    if (foundDevice != null) {
                        onBonded(foundDevice)
                    }
                } else {
                    Toast.makeText(context, "You must enable bluetooth in order to setup sensors.", Toast.LENGTH_LONG).show()
                }

            else -> println("Request with code $requestCode not recognized")
        }
    }

    fun requestLocationPermissions(block: () -> Unit): Unit {
        Toast.makeText(context, "Checking bluetooth and location permissions", Toast.LENGTH_LONG).show()

        return doOrWarnIfActivityNotExists({ nonNullActivity ->
            val applicationContext: Context = context!!

            if (ContextCompat.checkSelfPermission(nonNullActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PermissionChecker.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(nonNullActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }

            if (ContextCompat.checkSelfPermission(nonNullActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PermissionChecker.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(nonNullActivity,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) == PermissionChecker.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(nonNullActivity,
                        arrayOf(Manifest.permission.BLUETOOTH_ADMIN), 1)
                }
                if (ContextCompat.checkSelfPermission(nonNullActivity,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    block()
                } else {
                    Toast.makeText(applicationContext, "You must enable bluetooth and location permissions", Toast.LENGTH_LONG).show()
                }
            }
        }, "Can't check for bluetooth permissions, activity doesn't exist")
    }

    fun onFound(device: BluetoothDevice): Unit {
        val deviceHardwareAddress = device.address
        if (!foundDevices.contains(deviceHardwareAddress)) {
            val viewModel = DeviceViewModel(
                View.generateViewId(),
                device
            )
            foundDevices += Pair(deviceHardwareAddress, viewModel)

            renderDeviceSetupButton(device)

            val bonding = device.createBond()
            Toast.makeText(context, "Found ${device.name}, starting bonding with ${device.name}", Toast.LENGTH_LONG)
            println("Bonding status with ${device.name}: $bonding")
        }
    }

    fun onBonded(device: BluetoothDevice): Unit {
        Toast.makeText(context, "Paired with ${device.name}. Proceed by pressing the ${device.name} setup button.", Toast.LENGTH_LONG)
    }

    fun setupBluetoothButtons(view: View): Unit {
        println("Setting up bluetooth buttons")
        getDiscoverButton(view)?.setOnClickListener(View.OnClickListener {
            bluetoothAdapter.startDiscovery()
        })
    }

    private fun getDiscoverButton(view: View?): Button? {
        if (view != null) {
            return view?.findViewById<Button>(R.id.discovery_button)
        } else {
            println("Couldn't find the discovery button. Returning nothing.")
        }
        return null
    }


    private fun getDeviceSetupButton(device: BluetoothDevice): LinearLayout? {
        val viewId = foundDevices.get(device.address)?.id
        if (viewId != null) {
            return parentLayout.findViewById(viewId)
        }
        println("Couldn't get device setup button in view. Returning null")
        return null
    }

    fun renderDeviceSetupButton(device: BluetoothDevice): Unit {
        println("Adding new device ${device.name} with hash ${device.hashCode()} to parent view")
        val buttons = layoutInflater.inflate(R.layout.pair_management_buttons, null)
        val viewModel = foundDevices.get(device.address)
        if (viewModel != null) {
            buttons.id = viewModel.id
            buttons.findViewById<TextView>(R.id.device_name).text = device.address
            buttons.findViewById<TextView>(R.id.pair_status).text = getDeviceBondStateLabel(device.bondState)

            buttons.findViewById<Button>(R.id.pair_button).setOnClickListener { setupDevice(device) }
            buttons.findViewById<Button>(R.id.remove_button).setOnClickListener { removeDevice(device) }

            parentLayout
                .findViewById<LinearLayout>(R.id.peripheral_setup_page)
                .addView(buttons)

            println("Finished adding device ${device.name} with hash ${device.hashCode()} from parent view")
        } else {
            println("Couldn't find device ${device.address} in found devices")
        }
    }

    private fun updateDeviceBondingState(device: BluetoothDevice): Unit {
        val setupButtonView = getDeviceSetupButton(device)
        getOrWarn<TextView>(setupButtonView?.findViewById<TextView>(R.id.pair_status), {
            pairStatusView  ->
                pairStatusView.text = getDeviceBondStateLabel(device.bondState)
        }, "Couldn't update bonding state view for device ${device.name} ${device.address}")
    }

    private fun getDeviceBondStateLabel(bondState: Int): String {
        return when(bondState) {
            BluetoothDevice.BOND_BONDED -> "paired"
            BluetoothDevice.BOND_BONDING -> "pairing"
            BluetoothDevice.BOND_NONE -> "unpaired"
            else -> {
                println("Unforseen bond state received: ${bondState}")
                return ""
            }
        }
    }

    private fun setupDevice(device: BluetoothDevice): Unit {
        Thread(Runnable { peripheralSetupClient.setupDevice(device) }).start()
    }

    fun removeDevice(device: BluetoothDevice): Unit {
        println("Removing device ${device.name} with hash ${device.address} to parent view")
        val viewModel = foundDevices.get(device.address)
        if (viewModel != null) {
            foundDevices -= device.address
            val deviceUIId = viewModel.id
            parentLayout
                .findViewById<LinearLayout>(R.id.peripheral_setup_page)
                .removeView(parentLayout.findViewById<LinearLayout>(deviceUIId))
        }
    }

    private fun setupBluetoothAdapter(): Unit {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            throw Exception("This device doesn't support Bluetooth. Crash")
        } else {
            bluetoothAdapter = adapter
            if (adapter.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                onActivityResult(REQUEST_ENABLE_BT, -1, null)
            }
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
            println(warning)
        }
    }

}