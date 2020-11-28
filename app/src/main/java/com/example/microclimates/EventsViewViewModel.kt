package com.example.microclimates

import androidx.lifecycle.*
import api.Events
import api.PeripheralOuterClass
import java.util.*

class EventsViewViewModel : ViewModel() {
    private val selectedPeripheral: MutableLiveData<PeripheralOuterClass.Peripheral?> = MutableLiveData()
    private val peripheralEvents: MutableLiveData<Map<String, List<Events.MeasurementEvent>>> = MutableLiveData(mutableMapOf())
    private val startDate: MutableLiveData<Date> = MutableLiveData()
    private val endDate: MutableLiveData<Date> = MutableLiveData()
    private val dateRange = MediatorLiveData<Pair<Date?, Date?>>()
    private val eventSliceSpec = MediatorLiveData<Pair<Pair<Date?, Date?>?, PeripheralOuterClass.Peripheral?>>()

    init {
        dateRange.addSource(startDate) { s -> dateRange.value = Pair(s, dateRange.value?.second) }
        dateRange.addSource(endDate) { e -> dateRange.value = Pair(dateRange.value?.first, e) }

        eventSliceSpec.addSource(dateRange) { dr -> eventSliceSpec.value = Pair(dr, eventSliceSpec.value?.second)}
        eventSliceSpec.addSource(selectedPeripheral) { p ->
            eventSliceSpec.value = Pair(eventSliceSpec.value?.first, p)
        }
    }

    fun getEventSlice(): LiveData<Pair<Pair<Date?, Date?>?, PeripheralOuterClass.Peripheral?>> {
        return eventSliceSpec
    }

    fun setDateRange(newRange: Pair<Date, Date>): Unit {
        dateRange.value = newRange
    }

    fun getDateRange(): LiveData<Pair<Date?, Date?>> {
        return dateRange
    }

    fun setStartDate(date: Date): Unit {
        startDate.value = date
    }

    fun setEndDate(date: Date): Unit {
        endDate.value = date
    }

    fun setSelectedPeripheral(peripheral: PeripheralOuterClass.Peripheral?): Unit {
        selectedPeripheral.value = peripheral
    }

    fun setEvents(peripheralId: String, newEvents: List<Events.MeasurementEvent>): Unit {
        val oldPeripheralEvents = peripheralEvents.value
        if (oldPeripheralEvents == null) {
            peripheralEvents.value = mutableMapOf(Pair(peripheralId, newEvents))
        } else {
            peripheralEvents.value = oldPeripheralEvents + Pair(peripheralId, newEvents)
        }
    }
}

