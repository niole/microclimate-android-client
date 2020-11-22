package com.example.microclimates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import api.Events
import api.PeripheralOuterClass

class EventsViewViewModel : ViewModel() {
    private val selectedPeripheral: MutableLiveData<PeripheralOuterClass.Peripheral?> = MutableLiveData()
    private val peripheralEvents: MutableLiveData<Map<String, List<Events.MeasurementEvent>>> = MutableLiveData(mutableMapOf())

    fun getLivePeripheralEvents(peripheralId: String): LiveData<List<Events.MeasurementEvent>?> {
        return Transformations.map(peripheralEvents) { it.get(peripheralId) }
    }

    fun getPeripheralEvents(peripheralId: String): List<Events.MeasurementEvent>? {
        return peripheralEvents.value?.get(peripheralId)
    }

    fun setSelectedPeripheral(peripheral: PeripheralOuterClass.Peripheral?): Unit {
        selectedPeripheral.value = peripheral
    }

    fun getSelectedPeripheral(): LiveData<PeripheralOuterClass.Peripheral?> {
        return selectedPeripheral
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
