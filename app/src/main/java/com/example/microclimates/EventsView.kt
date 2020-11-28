package com.example.microclimates

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import api.Events
import api.PeripheralOuterClass
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.protobuf.Timestamp
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.Period
import java.time.temporal.TemporalUnit
import java.util.*
import java.util.concurrent.TimeUnit

class EventsView : Fragment() {
    private val LOG_TAG  = "EventsView"
    private val lineColors = listOf(Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW, Color.BLACK)
    private val coreStateViewModel: CoreStateViewModel by activityViewModels()
    private val model: EventsViewViewModel by viewModels()

    lateinit var defaultStartDate: Date
    private val defaultEndDate: Date = Calendar.getInstance().time

    init {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, -1)
        defaultStartDate = startDate.time
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (model.getEndDate().value == null) {
            model.setEndDate(defaultEndDate)
        }

        if (model.getStartDate().value == null) {
            model.setStartDate(defaultStartDate)
        }

        model.getSelectedPeripheral().observe({ lifecycle }) {
            Log.i(LOG_TAG, "Peripheral selection changed")
            val deployment = coreStateViewModel.getDeployment().value
            val peripheral = it
            val dateRange = model.getDateRange().value
            if (deployment != null && peripheral != null) {
                fetchPeripheralEvents(
                    deploymentId = deployment.id,
                    peripheral = peripheral,
                    startDate = dateRange?.first,
                    endDate = dateRange?.second
                )
            }
        }

        model.getDateRange().observe({ lifecycle}) {
            Log.i(LOG_TAG, "Date range selection changed")
            val deployment = coreStateViewModel.getDeployment().value
            val peripheral = model.getSelectedPeripheral().value
            if (deployment != null && peripheral != null) {
                fetchPeripheralEvents(
                    deploymentId = deployment.id,
                    peripheral = peripheral,
                    startDate = it.first,
                    endDate = it.second
                )
            }
        }

        val peripherals = coreStateViewModel.getPeripherals().value
        peripherals?.forEach { peripheral ->
            model.getLivePeripheralEvents(peripheral.id).observe({ lifecycle }) {events ->
                if (events != null) {
                    addLine(peripheral, events)
                } else {
                    Log.w(LOG_TAG, "Couldn't add chart line for ${peripheral.id}. No events exist for it.")
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_events_view, container, false)

        val deployment = coreStateViewModel.getDeployment().value
        val peripherals = coreStateViewModel.getPeripherals().value

        val endDate = model.getEndDate().value ?: defaultEndDate
        val startDate = model.getStartDate().value ?: defaultStartDate

        val startButton = DatePickerButton.newInstance(DateRangePicker.parser.format(startDate.time), "from: ")
        val endButton = DatePickerButton.newInstance(DateRangePicker.parser.format(endDate.time), "to: ")

        val t = childFragmentManager.beginTransaction()
        t.add(R.id.date_pickers, startButton)
        t.add(R.id.date_pickers, endButton)
        t.addToBackStack(null)
        t.commit()

        startButton.setOnChangeListener {
            val endDate = model.getEndDate().value
            if (endDate != null && it > endDate) {
                throw IllegalArgumentException("Start date must come before end date")
            } else {
                model.setStartDate(it)
            }
        }

        endButton.setOnChangeListener {
            val startDate = model.getStartDate().value
            if (startDate != null && it < startDate) {
                throw IllegalArgumentException("End date must come after start date")
            } else {
                model.setEndDate(it)
            }
        }

        if (deployment != null && peripherals != null) {
            val spinner = inflatedView.findViewById<Spinner>(R.id.peripheral_events_selector)
            if (spinner != null) {
                // TODO names are not unique
                val peripheralNames = peripherals.map { it.name }
                val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, peripheralNames)
                spinner.adapter = adapter

                spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        val selectedName = peripheralNames[position]
                        val peripheral = peripherals.find { it.name == selectedName }
                        val pType = peripheral?.type
                        val pUnit = peripheral?.unit
                        model.setSelectedPeripheral(peripheral)
                        inflatedView.findViewById<TextView>(R.id.peripheral_details).text = "$pType ($pUnit)"

                    }
                })
            }

        } else {
            Log.e(LOG_TAG, "Couldn't build events chart. No deployment exists in core state.")
        }

        return inflatedView
    }

    private fun fetchPeripheralEvents(
        deploymentId: String,
        peripheral: PeripheralOuterClass.Peripheral,
        startDate: Date?,
        endDate: Date?
    ): Unit {
        Log.i(LOG_TAG, "Fetching events for deployment $deploymentId, peripheral ${peripheral.id}, start date $startDate, end date $endDate")
        Thread(Runnable {
            val eventsChannel = Channels.eventsChannel()
            val peripheralId = peripheral.id
            try {
                val startTime = Timestamp
                    .newBuilder()
                    .setSeconds((startDate?.time ?: defaultStartDate.time) / 1000)
                    .build()

                val endTime = Timestamp
                    .newBuilder()
                    .setSeconds((endDate?.time ?: defaultEndDate.time) / 1000)
                    .build()

                val filterRequest = Events.MeasurementEventFilterRequest
                    .newBuilder()
                    .setPeripheralId(peripheralId)
                    .setDeploymentId(deploymentId)
                    .setStartTime(startTime)
                    .setEndTime(endTime)
                    .build()

                val allEvents = Stubs.eventsStub(eventsChannel).filterEvents(filterRequest)

                if (allEvents != null) {
                    val eventsList = allEvents.asSequence().toList()
                    view?.post {
                        model.setEvents(peripheralId, eventsList)

                        eventsChannel.shutdown()
                        eventsChannel.awaitTermination(10, TimeUnit.SECONDS)
                    }
                }
            } catch (t: Throwable) {
                Log.e(
                    LOG_TAG,
                    "Failed to fetch events for peripheral $peripheral. message: ${t.message}, cause: ${t.cause}"
                )
                eventsChannel.shutdown()
                eventsChannel.awaitTermination(10, TimeUnit.SECONDS)
            }
        }).start()
    }

    private fun addLine(peripheral: PeripheralOuterClass.Peripheral, events: List<Events.MeasurementEvent>): Unit {
        val chart = view?.findViewById<LineChart>(R.id.line_chart_ui)
        if (chart != null) {
            val chartableEntries = events.map {
                Entry(it.timeStamp.seconds.toFloat(), it.value.toFloat())
            }

            val dataset = LineDataSet(chartableEntries, peripheral.name)
            dataset.color = lineColors[Math.abs(peripheral.id.hashCode()) % lineColors.size]
            dataset.valueTextColor = Color.BLACK
            val lineData = LineData(dataset)
            chart.data = lineData

            val xAxis = chart.xAxis
            xAxis.position = XAxisPosition.BOTTOM
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLines(false)

            xAxis.valueFormatter = XAxisFormatter()
            chart.invalidate()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return EventsView()
        }
    }
}

class XAxisFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date(value.toLong() * 1000))
    }
}
