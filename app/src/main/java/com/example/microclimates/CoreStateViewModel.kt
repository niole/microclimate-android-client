package com.example.microclimates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import api.DeploymentOuterClass
import api.PeripheralOuterClass
import api.UserOuterClass.User

class CoreStateViewModel : ViewModel() {
    private val owner: MutableLiveData<User?> = MutableLiveData()
    private val deployment: MutableLiveData<DeploymentOuterClass.Deployment?> = MutableLiveData()
    private val peripherals: MutableLiveData<List<PeripheralOuterClass.Peripheral>> = MutableLiveData(listOf())

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

    fun setPeripherals(newPeripherals: List<PeripheralOuterClass.Peripheral>): Unit {
        peripherals.value = newPeripherals
    }

    fun getPeripherals(): LiveData<List<PeripheralOuterClass.Peripheral>> {
        return peripherals
    }

}
