package com.example.microclimates

import androidx.lifecycle.ViewModel
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class SetupPageViewModel : ViewModel() {
    private var bluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData()
    private var foundDevices: MutableLiveData<Map<String, DeviceViewModel>> = MutableLiveData()

    init {
        Log.i("SetupPageViewModel", "SetupPageViewModel created!")
        bluetoothEnabled.value = false
        foundDevices.value = mutableMapOf()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("SetupPageViewModel", "SetupPageViewModel destroyed!")
    }

    fun getDevices(): LiveData<Map<String, DeviceViewModel>> {
        return foundDevices
    }

    fun setDevices(devices: Map<String, DeviceViewModel>): Unit {
        foundDevices.value = devices
    }

    fun getBluetoothEnabled(): LiveData<Boolean> {
        return bluetoothEnabled
    }

    fun setBluetoothEnabled(isEnabled: Boolean): Unit {
        bluetoothEnabled.value = isEnabled
    }

    fun setPairing(deviceToUpdate: BluetoothDevice): Unit {
        var devices = foundDevices.value
        val device = devices?.get(deviceToUpdate.address)
        if (devices != null && device != null) {
            devices += Pair(deviceToUpdate.address, DeviceViewModel(
                device.id,
                BondStatus.PAIRING,
                deviceToUpdate
            ))
            foundDevices.value = devices
        }
    }

    fun setPairingFailed(deviceToUpdate: BluetoothDevice): Unit {
        var devices = foundDevices.value
        val device = devices?.get(deviceToUpdate.address)
        if (devices != null && device != null) {
            devices += Pair(deviceToUpdate.address, DeviceViewModel(
                device.id,
                BondStatus.NOT_PAIRED,
                deviceToUpdate
            ))
            foundDevices.value = devices
        }
    }

    fun addDevice(deviceToAdd: BluetoothDevice): Unit {
        var devices = foundDevices.value
        if (devices == null) {
          devices = mutableMapOf()
        }
        val newDeviceModel = DeviceViewModel(
            View.generateViewId(),
            BondStatus.fromDeviceBondStatus(deviceToAdd.bondState),
            deviceToAdd
        )
        devices += Pair(newDeviceModel.device.address, newDeviceModel)
        foundDevices.value = devices
    }

    fun removeDevice(deviceModel: DeviceViewModel): Unit {
        var devices = foundDevices.value!!
        devices -= deviceModel.device.address
        foundDevices.value = devices
    }

    fun updateDevice(deviceToFind: BluetoothDevice): Unit {
        var devices = foundDevices.value
        val device = devices?.get(deviceToFind.address)
        if (devices != null && device != null) {
            devices += Pair(
                deviceToFind.address,
                DeviceViewModel(
                    device.id,
                    BondStatus.fromDeviceBondStatus(deviceToFind.bondState),
                    deviceToFind
                )
            )
            foundDevices.value = devices
        } else {
            Log.w(
                "UpdateDeviceStatus",
                "failed to update device status for $deviceToFind"
            )
        }
    }
}