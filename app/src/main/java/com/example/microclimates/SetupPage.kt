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

    var foundDevices: Map<String, BluetoothDevice> = mutableMapOf()

    lateinit var bluetoothAdapter: BluetoothAdapter

    val bluetoothEvents: BluetoothEventListener = BluetoothEventListener()

    lateinit var peripheralSetupClient: BluetoothPeripheralSetupClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        doOrWarnIfActivityNotExists({ ->
            activity?.unregisterReceiver(bluetoothEvents)
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
        return doOrWarnIfActivityNotExists({
            val applicationContext: Context = context!!
            val fragmentActivity = activity!!

            if (ContextCompat.checkSelfPermission(fragmentActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PermissionChecker.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(fragmentActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }

            if (ContextCompat.checkSelfPermission(fragmentActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PermissionChecker.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(fragmentActivity,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) == PermissionChecker.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(fragmentActivity,
                        arrayOf(Manifest.permission.BLUETOOTH_ADMIN), 1)
                }
                if (ContextCompat.checkSelfPermission(fragmentActivity,
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
            foundDevices += Pair(deviceHardwareAddress, device)

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setup_page, container, false)

        setupBluetoothButtons(view)
        bluetoothEvents.setOnFoundHandler { onFound(it) }
        bluetoothEvents.setOnBondedHandler { onBonded(it) }
        bluetoothEvents.setOnDiscoveryStartedHandler { getDiscoverButton(view)?.text = "x" }
        bluetoothEvents.setOnDiscoveryStoppecHandler { getDiscoverButton(view)?.text = "+" }
        bluetoothEvents.setOnDeviceBondStateChanged { device -> updateDeviceBondingState(device)}
        peripheralSetupClient = BluetoothPeripheralSetupClient(view.findViewById(R.id.peripheral_setup_page))

        return view
    }

    companion object {
        val SENSOR_DEVICE_NAME = "raspberrypi"

        fun newInstance() = SetupPage()
    }

    private fun getDeviceSetupButton(device: BluetoothDevice): LinearLayout? {
        if (view != null) {
            val deviceUIId = Math.abs(device.hashCode())
            return view?.findViewById(deviceUIId)
        }
        println("Couldn't get device setup button in view. Returning null")
        return null
    }

    fun renderDeviceSetupButton(device: BluetoothDevice): Unit {
        doOrWarnIfViewNotExists({ ->
            println("Adding new device ${device.name} with hash ${device.hashCode()} to parent view")
            println(device)
            val buttons = layoutInflater.inflate(R.layout.pair_management_buttons, (view as ViewGroup))
            val deviceUIId = Math.abs(device.hashCode())
            buttons.id = deviceUIId
            buttons.findViewById<TextView>(R.id.device_name).text = device.name
            buttons.findViewById<TextView>(R.id.pair_status).text = getDeviceBondStateLabel(device.bondState)
            buttons.findViewById<Button>(R.id.pair_button).setOnClickListener { setupDevice(device) }
            buttons.findViewById<Button>(R.id.remove_button).setOnClickListener { removeDevice(device) }

            println("Finished adding device ${device.name} with hash ${device.hashCode()} from parent view")
        }, "View was null when attempting to add pairing button for device ${device.name}")
    }

    private fun updateDeviceBondingState(device: BluetoothDevice): Unit {
        val pairStatusView = getDeviceSetupButton(device)?.findViewById<TextView>(R.id.pair_status)
        pairStatusView?.text = getDeviceBondStateLabel(device.bondState)
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
        Thread(Runnable {
            peripheralSetupClient.setupDevice(device)
        }).start()
    }

    fun removeDevice(device: BluetoothDevice): Unit {
        doOrWarnIfViewNotExists({ ->
            println("Removing device ${device.name} with hash ${device.hashCode()} to parent view")

            foundDevices -= device.address
            val deviceUIId = Math.abs(device.hashCode())
            val buttonGroup = view?.findViewById<LinearLayout>(deviceUIId)
            (buttonGroup?.parent as ViewGroup).removeView(buttonGroup)

            println("Finished removing device ${device.name} with hash ${device.hashCode()} to parent view")

        }, "Could not remove device from view, view does not exist")
    }

    private fun doOrWarnIfViewNotExists(block: () -> Unit, warning: String): Unit {
        if (view != null) {
            return block()
        } else {
            println(warning)
        }
    }

    fun setupBluetoothAdapter(): Unit {
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
        doOrWarnIfActivityNotExists({ ->
            val filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            filter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)

            activity?.registerReceiver(bluetoothEvents, filter)
        }, "Activity doesn't exist, couldn't register bluetooth events")
    }

    fun doOrWarnIfActivityNotExists(block: () -> Unit, warning: String): Unit {
        if (activity != null) {
            return block()
        } else {
            println(warning)
        }
    }
}