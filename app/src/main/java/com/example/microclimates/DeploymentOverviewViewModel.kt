package com.example.microclimates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import api.DeploymentOuterClass
import api.PeripheralOuterClass.Peripheral
import api.UserOuterClass.User

class DeploymentOverviewViewModel : ViewModel() {
    private val owner: MutableLiveData<User?> = MutableLiveData()
    private val deployment: MutableLiveData<DeploymentOuterClass.Deployment?> = MutableLiveData()
    private val peripherals: MutableLiveData<List<Peripheral>> = MutableLiveData(listOf())

    fun setPeripherals(newPeripherals: List<Peripheral>): Unit {
        peripherals.value = newPeripherals
    }

    fun getPeripherals(): LiveData<List<Peripheral>> {
        return peripherals
    }

    fun setOwner(foundOwner: User): Unit {
        owner.value = foundOwner
    }

    fun getOwner(): LiveData<User?> {
        return owner
    }

    fun setDeployment(foundDeployment: DeploymentOuterClass.Deployment): Unit {
        deployment.value = foundDeployment
    }

    fun getDeployment(): LiveData<DeploymentOuterClass.Deployment?> {
        return deployment
    }
}