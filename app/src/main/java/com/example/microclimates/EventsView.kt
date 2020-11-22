package com.example.microclimates

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class EventsView : Fragment() {
    private val LOG_TAG  = "EventsView"
    private val lineColors = listOf(Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW, Color.BLACK)
    private val coreStateViewModel: CoreStateViewModel by activityViewModels()
    private val model: EventsViewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_events_view, container, false)

        val deployment = coreStateViewModel.getDeployment().value
        val peripherals = coreStateViewModel.getPeripherals().value
        if (deployment != null && peripherals != null) {
            refetchAllEvents(deployment.id, peripherals)
        } else {
            Log.e(LOG_TAG, "Couldn't build events chart. No deployment exists in core state.")
        }

        return inflatedView
    }

    private fun refetchAllEvents(deploymentId: String, peripherals: List<PeripheralOuterClass.Peripheral>): Unit {
        Thread(Runnable {
            val eventsChannel = Channels.eventsChannel()
            peripherals.forEach { peripheral ->
                val peripheralId = peripheral.id
                try {
                    val startTime = Timestamp
                        .newBuilder()
                        .setSeconds(0L)
                        .build()

                    val endTime = Timestamp
                        .newBuilder()
                        .setSeconds(System.currentTimeMillis() / 1000)
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
                            addLine(peripheral, eventsList)
                            if (peripheralId == peripherals.last().id) {
                                eventsChannel.shutdown()
                                eventsChannel.awaitTermination(10, TimeUnit.SECONDS)
                            }
                        }
                    }
                } catch (t: Throwable) {
                    Log.e(
                        LOG_TAG,
                        "Failed to fetch all events. message: ${t.message}, cause: ${t.cause}"
                    )
                    if (peripheralId == peripherals.last().id) {
                        eventsChannel.shutdown()
                        eventsChannel.awaitTermination(10, TimeUnit.SECONDS)
                    }
                }
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
            dataset.color = lineColors[peripheral.id.hashCode() % lineColors.size]
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
