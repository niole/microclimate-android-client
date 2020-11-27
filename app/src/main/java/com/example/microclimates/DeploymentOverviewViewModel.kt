package com.example.microclimates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DeploymentOverviewViewModel : ViewModel() {
    private val connectedPeripherals: MutableLiveData<List<LivePeripheralModel>> = MutableLiveData(listOf())

    fun getConnectedPeripherals(): LiveData<List<LivePeripheralModel>> {
        return connectedPeripherals
    }

    fun setNewConnectedPeripherals(newPeripherals: List<LivePeripheralModel>): Unit {
        connectedPeripherals.value = newPeripherals
    }

}