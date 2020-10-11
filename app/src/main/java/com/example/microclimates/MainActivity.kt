package com.example.microclimates

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.BluetoothAdapter.*
import android.bluetooth.BluetoothDevice.*
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_DENIED
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.view.*
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button

class MainActivity : AppCompatActivity() {

    val REQUEST_ENABLE_BT = 1

    companion object {

        val SENSOR_DEVICE_NAME = "raspberrypi"
    }

    var foundDevices: Map<String, BluetoothDevice> = mutableMapOf()

    lateinit var bluetoothAdapter: BluetoothAdapter

    val bluetoothEvents: BluetoothEventListener = BluetoothEventListener()

    lateinit var peripheralSetupClient: BluetoothPeripheralSetupClient

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

        bluetoothEvents.setOnFoundHandler { onFound(it) }
        bluetoothEvents.setOnBondedHandler { onBonded(it) }
        peripheralSetupClient = BluetoothPeripheralSetupClient(findViewById(R.id.main_content))

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
            Toast.makeText(applicationContext, "Acquired bluetooth and location permissions", Toast.LENGTH_LONG).show()
            registerBluetoothEvents()
            setupBluetoothAdapter()
            setupBluetoothButtons()
        }
    }

    fun removeDevice(device: BluetoothDevice): Unit {
        println("Removing device ${device.name} with hash ${device.hashCode()} to parent view")
        foundDevices -= device.address
        val deviceUIId = Math.abs(device.hashCode())
        val buttonToRemove = findViewById<Button>(deviceUIId)
        (buttonToRemove.parent as ViewGroup).removeView(buttonToRemove)
        println("Finished removing device ${device.name} with hash ${device.hashCode()} to parent view")
    }

    fun renderDeviceSetupButton(device: BluetoothDevice): Unit {
        println("Adding new device ${device.name} with hash ${device.hashCode()} to parent view")
        val deviceUIId = Math.abs(device.hashCode())
        val parentLayout = findViewById<CoordinatorLayout>(R.id.main_content)
        val button = Button(this)

        button.setTextColor(Color.BLACK)
        button.setBackgroundColor(Color.WHITE)
        button.text = "Setup ${device.name}"

        val layoutParams = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.gravity = Gravity.TOP
        layoutParams.setMargins(0, 140, 0, 0)
        button.layoutParams = CoordinatorLayout.LayoutParams(layoutParams)

        button.setId(deviceUIId)
        button.setOnClickListener { peripheralSetupClient.setupDevice(device) }
        parentLayout.addView(button)
        println("Finished adding device ${device.name} with hash ${device.hashCode()} from parent view")
    }

    fun onFound(device: BluetoothDevice): Unit {
        val deviceHardwareAddress = device.address
        foundDevices += Pair(deviceHardwareAddress, device)

        renderDeviceSetupButton(device)

        val bonding = device.createBond()
        Toast.makeText(applicationContext, "Found ${device.name}, starting bonding with ${device.name}", Toast.LENGTH_LONG)
        println("Bonding status with ${device.name}: $bonding")
    }

    fun onBonded(device: BluetoothDevice): Unit {
        Toast.makeText(applicationContext, "Paired with ${device.name}. Proceed by pressing the ${device.name} setup button.", Toast.LENGTH_LONG)
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
                        Toast.makeText(applicationContext, "Bluetooth enabled.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "You must enable bluetooth in order to setup sensors.", Toast.LENGTH_LONG).show()
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

    fun setupBluetoothButtons(): Unit {
        val startDiscoveryButton = findViewById<Button>(R.id.start_discovery) // TODO enable disable at right times

        startDiscoveryButton.setOnClickListener(View.OnClickListener {
            bluetoothAdapter.startDiscovery()
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
        Toast.makeText(applicationContext, "Checking bluetooth and location permissions", Toast.LENGTH_LONG).show()

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
