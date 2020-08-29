package com.example.microclimates

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.BluetoothAdapter.*
import android.bluetooth.BluetoothDevice.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.os.ParcelUuid
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_DENIED
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import android.widget.Button
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


class MainActivity : AppCompatActivity() {
    val REQUEST_ENABLE_BT = 1

    companion object {

        val SENSOR_DEVICE_NAME = "raspberrypi"
    }

    lateinit var bluetoothAdapter: BluetoothAdapter

    var bluetoothEvents: BluetoothEventListener = BluetoothEventListener()

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothEvents.setOnFoundHandler {
            onFound(it)
        }

        bluetoothEvents.setOnBondedHandler {
            onBonded(it)
        }

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        requestLocationPermissions {
            registerBluetoothEvents()
            setupBluetoothAdapter()
            setupBluetoothTriggers()
        }
    }

    fun onFound(device: BluetoothDevice): Unit {
        val deviceHardwareAddress = device.address
        println(deviceHardwareAddress)

        val bonding = device.createBond()

        println("Bonding $bonding")
    }

    fun onBonded(device: BluetoothDevice): Unit {
        Toast.makeText(applicationContext, "${device.name} is paired", Toast.LENGTH_LONG)
    }

    fun triggerInformationExchange() {
        var message = ""
        try {
            val serverSocket: BluetoothServerSocket =
                bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                    "mysocket",
                    UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee")
                )

            val connectedSocket: BluetoothSocket = serverSocket.accept(30000)
            println("Connected a bluetooth socket")
            serverSocket.close()
            println("Closed server socket")

            val inputStream = connectedSocket.inputStream

            println("Input stream created")

            val bufferSize = 1024
            val buffer = ByteArray(bufferSize)
            var bytesRead = -1

            while(true) {
                println("attempting to read")
                bytesRead = inputStream.read(buffer)

                if (bytesRead == -1) {
                    break
                }

                println("read some bytes")
                val str: String = String(buffer)

                message += str
            }

            connectedSocket.close()

        } catch (error: Exception) {
            println("Something happened to the bluetooth server: $error")
            Toast.makeText(applicationContext, "Failed to connect to socket: $error", Toast.LENGTH_LONG)
        } finally {
            println("found message $message")
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(bluetoothEvents)
    }

    override fun onResume() {
        super.onResume()
        registerBluetoothEvents()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {

            REQUEST_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK) {

                    val bondedDevices = bluetoothAdapter.bondedDevices
                    val foundDevice = bondedDevices.find { it.name == SENSOR_DEVICE_NAME }
                    if (foundDevice != null) {
                        onBonded(foundDevice)
                    } else {
                        Toast.makeText(applicationContext, "Bluetooh enabled. Looking for sensors", Toast.LENGTH_LONG).show()
                        bluetoothAdapter.startDiscovery()
                    }
                } else {
                    Toast.makeText(applicationContext, "You must enable bluetooth", Toast.LENGTH_LONG).show()
                }

            else -> println("Request with code $requestCode not recognized")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            //rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    fun setupBluetoothTriggers(): Unit {
        val startDiscoveryButton = findViewById<Button>(R.id.start_discovery)
        val stopDiscoveryButton = findViewById<Button>(R.id.stop_discovery)
        val bltClientContact = findViewById<Button>(R.id.contact_BLT_client)

        bltClientContact.setOnClickListener {
            triggerInformationExchange()
        }

        startDiscoveryButton.setOnClickListener(View.OnClickListener {
            bluetoothAdapter.startDiscovery()
        })

        stopDiscoveryButton.setOnClickListener(View.OnClickListener {
            bluetoothAdapter.cancelDiscovery()
        })


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
        val filter = IntentFilter()
        filter.addAction(ACTION_PAIRING_REQUEST)
        filter.addAction(ACTION_DISCOVERY_STARTED)
        filter.addAction(ACTION_DISCOVERY_FINISHED)
        filter.addAction(ACTION_REQUEST_ENABLE)
        filter.addAction(ACTION_FOUND)
        filter.addAction(ACTION_BOND_STATE_CHANGED)

        registerReceiver(bluetoothEvents, filter)
    }

    fun requestLocationPermissions(block: () -> Unit): Unit {
        if (ContextCompat.checkSelfPermission(this@MainActivity, ACCESS_FINE_LOCATION) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        if (ContextCompat.checkSelfPermission(this@MainActivity, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this@MainActivity, BLUETOOTH_ADMIN) == PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.BLUETOOTH_ADMIN), 1)
            }
            if (ContextCompat.checkSelfPermission(this@MainActivity, BLUETOOTH_ADMIN) == PERMISSION_GRANTED) {
                return block()
            }
        }

        Toast.makeText(applicationContext, "You must enable bluetooth and location permissions", Toast.LENGTH_LONG).show()
    }

}

class BluetoothEventListener : BroadcastReceiver() {

    var onFound: (BluetoothDevice) -> Unit = {}

    var onBonded: (BluetoothDevice) -> Unit = {}

    fun setOnFoundHandler(value: (BluetoothDevice) -> Unit) {
        onFound = value
    }

    fun setOnBondedHandler(value: (BluetoothDevice) -> Unit) {
        onBonded = value
    }

    override fun onReceive(context: Context?, nullableIntent: Intent?) {
        if (nullableIntent != null) {
            val intent = nullableIntent!!
            val action: String = intent.action

            when(action) {
                ACTION_PAIRING_REQUEST  -> {
                    println("PAIRING REQUEST")
                    BluetoothDevice.EXTRA_PAIRING_KEY
                    BluetoothDevice.EXTRA_PAIRING_VARIANT
                }
                ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val currentBondState = device.getBondState()

                    val bondingStatus = when(currentBondState) {
                        BOND_BONDED -> "BOND_BONDED"
                        BOND_BONDING -> "BOND_BONDING"
                        BOND_NONE -> "BOND_NONE"
                        else -> "didn't account for this bonding state $currentBondState"
                    }

                    println("ACTION_BOND_STATE_CHANGED: $currentBondState A.K.A. $bondingStatus")

                    if (currentBondState == BluetoothDevice.BOND_BONDED) {
                        onBonded(device)
                    }
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address

                    if (deviceName == MainActivity.SENSOR_DEVICE_NAME) {
                        println("found sensor device: deviceName $deviceName deviceHardwareAddress $deviceHardwareAddress")
                        Toast.makeText(context, "found the device", Toast.LENGTH_SHORT).show()
                        onFound(device)
                    }
                }
                else -> println("Not watching for action $action")
            }
        }
    }
}
