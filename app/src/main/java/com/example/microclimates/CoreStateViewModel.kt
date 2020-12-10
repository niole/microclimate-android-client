package com.example.microclimates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import api.DeploymentOuterClass
import api.Events
import api.PeripheralOuterClass
import api.UserOuterClass.User
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import java.util.concurrent.TimeUnit

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

    fun addPeripheral(newPeripheral: PeripheralOuterClass.Peripheral): Unit {
        peripherals.value = (peripherals.value ?: listOf()) + newPeripheral
    }

    fun getPeripherals(): LiveData<List<PeripheralOuterClass.Peripheral>> {
        return peripherals
    }

    fun getPeripheralById(peripheralId: String): PeripheralOuterClass.Peripheral? {
        return peripherals.value?.find { it.id == peripheralId }
    }

    fun removePeripheral(peripheralId: String): Unit {
        peripherals.value = peripherals.value?.filter { it.id != peripheralId }
    }

    fun refetchDeploymentPeripherals(deploymentId: String): Unit {
       val request = PeripheralOuterClass.GetDeploymentPeripheralsRequest
           .newBuilder()
           .setDeploymentId(deploymentId)
           .build()
        val perphChannel = Channels.peripheralChannel()
        peripherals.value = Stubs.peripheralStub(perphChannel).getDeploymentPeripherals(request).asSequence().toList()
        perphChannel.shutdownNow()
        perphChannel.awaitTermination(1, TimeUnit.SECONDS)
    }

}
