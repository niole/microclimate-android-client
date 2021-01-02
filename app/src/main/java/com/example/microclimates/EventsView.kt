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
import androidx.lifecycle.Transformations
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
import java.util.*
import java.util.concurrent.TimeUnit

class EventsView : Fragment() {
    private lateinit var stubs: Stubs
    private val LOG_TAG  = "EventsView"
    private val lineColors = listOf(Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW, Color.BLACK)
    private val coreStateViewModel: CoreStateViewModel by activityViewModels()
    private val model: EventsViewViewModel by viewModels()
    lateinit var defaultDateRange: Pair<Date, Date>

    init {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, -1)
        defaultDateRange  = Pair(startDate.time, Calendar.getInstance().time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stubs = Stubs(requireContext())

        if (model.getDateRange().value == null) {
            model.setDateRange(defaultDateRange)
        }

        model.getEventSlice().observe({ lifecycle }) {
            val dateRange = it?.first ?: defaultDateRange
            val peripheral = it?.second
            val deployment = coreStateViewModel.getDeployment().value
            if (deployment != null && peripheral != null) {
                fetchPeripheralEvents(
                    deploymentId = deployment.id,
                    peripheral = peripheral,
                    startDate = dateRange.first!!,
                    endDate = dateRange.second!!
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_events_view, container, false)

        val dateRange = model.getDateRange().value ?: defaultDateRange
        val endDate = dateRange.second!!
        val startDate = dateRange.first!!

        val startButton = DatePickerButton.newInstance(DateRangePicker.parser.format(startDate.time), "from: ")
        val endButton = DatePickerButton.newInstance(DateRangePicker.parser.format(endDate.time), "to: ")

        val t = childFragmentManager.beginTransaction()
        t.replace(R.id.start_picker, startButton)
        t.replace(R.id.end_picker, endButton)
        t.addToBackStack(null)
        t.commit()

        startButton.setOnChangeListener {
            val endDate = model.getDateRange().value?.second
            if (endDate != null && it > endDate) {
                throw IllegalArgumentException("Start date must come before end date")
            } else {
                model.setStartDate(it)
            }
        }

        endButton.setOnChangeListener {
            val startDate = model.getDateRange().value?.first
            if (startDate != null && it < startDate) {
                throw IllegalArgumentException("End date must come after start date")
            } else {
                model.setEndDate(it)
            }
        }

        Transformations.switchMap(coreStateViewModel.getDeployment()) {
            coreStateViewModel.getPeripherals()
        }.observe({ lifecycle }) {peripherals ->
            val spinner = inflatedView.findViewById<Spinner>(R.id.peripheral_events_selector)
            if (spinner != null) {
                spinner.adapter = object : ArrayAdapter<PeripheralOuterClass.Peripheral>(requireContext(), R.layout.peripheral_spinner_item, peripherals) {
                    override fun getDropDownView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup
                    ): View {
                        return getView(position, convertView, parent)
                    }

                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = inflater.inflate(R.layout.peripheral_spinner_item, null)!!
                        val p = getItem(position)
                        view.findViewById<TextView>(R.id.name)?.text = p?.name
                        view.findViewById<TextView>(R.id.ptype)?.text = " (${p?.unit})"
                        return view
                    }
                }

                spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        model.setSelectedPeripheral(peripherals[position])
                    }
                })
            }

            val selectedPeripheral = model.getEventSlice().value?.second
            spinner.setSelection(Math.max(peripherals.indexOf(selectedPeripheral), 0))
        }

        return inflatedView
    }

    private fun fetchPeripheralEvents(
        deploymentId: String,
        peripheral: PeripheralOuterClass.Peripheral,
        startDate: Date,
        endDate: Date
    ): Unit {
        Log.i(LOG_TAG, "Fetching events for deployment $deploymentId, peripheral ${peripheral.id}, start date $startDate, end date $endDate")
        Thread(Runnable {
            val eventsChannel = Channels.getInstance(requireContext()).eventsChannel()
            val peripheralId = peripheral.id
            try {
                val startTime = Timestamp
                    .newBuilder()
                    .setSeconds(startDate.time / 1000)
                    .build()

                val endTime = Timestamp
                    .newBuilder()
                    .setSeconds(endDate.time / 1000)
                    .build()

                val filterRequest = Events.MeasurementEventFilterRequest
                    .newBuilder()
                    .setPeripheralId(peripheralId)
                    .setDeploymentId(deploymentId)
                    .setStartTime(startTime)
                    .setEndTime(endTime)
                    .build()

                val allEvents = stubs.eventsStub(eventsChannel).filterEvents(filterRequest)

                if (allEvents != null) {
                    val eventsList = allEvents.asSequence().toList()
                    view?.post {
                        model.setEvents(peripheralId, eventsList)

                        addLine(peripheral, eventsList)

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
            xAxis.labelCount = 4
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
        val pattern = "HH:mm MM/dd/yy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date(value.toLong() * 1000))
    }
}
