package com.example.microclimates

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import api.Events
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import com.google.protobuf.Timestamp
import java.util.concurrent.TimeUnit

class EventsViewViewModel : ViewModel() {
    private val LOG_TAG = "EventsViewViewModel"
    private val events: MutableLiveData<List<Events.MeasurementEvent>> = MutableLiveData(listOf())

    fun getAllEvents(): LiveData<List<Events.MeasurementEvent>> {
        return events
    }

    fun setEvents(newEvents: List<Events.MeasurementEvent>): Unit {
        events.value = newEvents
    }
}

