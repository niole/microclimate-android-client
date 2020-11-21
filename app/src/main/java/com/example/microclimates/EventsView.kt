package com.example.microclimates

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import api.Events
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.protobuf.Timestamp
import java.util.concurrent.TimeUnit

class EventsView : Fragment() {
    private val LOG_TAG  = "EventsView"
    private val coreStateViewModel: CoreStateViewModel by activityViewModels()
    private val model: EventsViewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_events_view, container, false)

        val deployment = coreStateViewModel.getDeployment().value
        val peripheralIds = coreStateViewModel.getPeripherals().value?.map { it.id }
        if (deployment != null && peripheralIds != null) {
            refetchAllEvents(deployment.id, peripheralIds)

            model.getAllEvents().observeForever { events ->
                val chart = inflatedView.findViewById<LineChart>(R.id.line_chart_ui)

                val chartableEntries = events.map {
                    Entry(it.timeStamp.seconds.toFloat(), it.value.toFloat())
                }

                val dataset = LineDataSet(chartableEntries, "Label sdf")
                dataset.color = 0
                dataset.valueTextColor = 1
                val lineData = LineData(dataset)
                chart.data = lineData
                chart.invalidate()
            }
        } else {
            Log.e(LOG_TAG, "Couldn't build events chart. No deployment exists in core state.")
        }

        return inflatedView
    }

    private fun refetchAllEvents(deploymentId: String, forPeripheralIds: List<String>): Unit {
        Thread(Runnable {
            val eventsChannel = Channels.eventsChannel()
            try {
                val startTime = Timestamp
                    .newBuilder()
                    .setSeconds(0L)
                    .build()
                val endTime = Timestamp.newBuilder().build()
                val filterRequest = Events.MeasurementEventFilterRequest
                    .newBuilder()
                    .addAllPeripheralIds(forPeripheralIds)
                    .setDeploymentId(deploymentId)
                    .setStartTime(startTime)
                    .setEndTime(endTime)
                    .build()

                val allEvents = Stubs.eventsStub(eventsChannel).filterEvents(filterRequest)

                if (allEvents != null) {
                    view?.post {
                        model.setEvents(allEvents.asSequence().toList())
                        eventsChannel.shutdown()
                        eventsChannel.awaitTermination(10, TimeUnit.SECONDS)
                    }
                }
            } catch (t: Throwable) {
                Log.e(LOG_TAG, "Failed to fetch all events. message: ${t.message}, cause: ${t.cause}")
                eventsChannel.shutdown()
                eventsChannel.awaitTermination(10, TimeUnit.SECONDS)
            }
        }).start()
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return EventsView()
        }
    }
}