package com.example.microclimates.api

import android.content.Context.MODE_PRIVATE
import api.DeploymentManagementServiceGrpc
import api.PeripheralManagementServiceGrpc
import api.PeripheralMeasurementEventServiceGrpc
import api.UserServiceGrpc
import io.grpc.*
import io.grpc.stub.MetadataUtils

class Stubs(val context: android.content.Context) {

    private fun getToken(): String? {
        return context.getSharedPreferences("microclimate-prefs", MODE_PRIVATE).getString("jwt", "fakedefault")
    }

    private fun getTokenHeader(): Metadata {
        val header = Metadata()
        val key = Metadata.Key.of("jwt-header-foo", Metadata.ASCII_STRING_MARSHALLER)
        val token = getToken()
        header.put(key, "jwt-prefix-foo$token")
        return header
    }

    fun peripheralStub(peripheralChannel: ManagedChannel): PeripheralManagementServiceGrpc.PeripheralManagementServiceBlockingStub {
        val header = getTokenHeader()
        val stub = PeripheralManagementServiceGrpc.newBlockingStub(peripheralChannel)
        return MetadataUtils.attachHeaders(stub, header)
    }

    fun blockingDeploymentStub(deploymentChannel: ManagedChannel): DeploymentManagementServiceGrpc.DeploymentManagementServiceBlockingStub {
        val header = getTokenHeader()
        val stub =  DeploymentManagementServiceGrpc.newBlockingStub(deploymentChannel)
        return MetadataUtils.attachHeaders(stub, header)
    }

    fun userStub(userChannel: ManagedChannel): UserServiceGrpc.UserServiceFutureStub {
        val header = getTokenHeader()
        val stub =  UserServiceGrpc.newFutureStub(userChannel)
        return MetadataUtils.attachHeaders(stub, header)
    }

    fun eventsStub(eventsChannel: ManagedChannel): PeripheralMeasurementEventServiceGrpc.PeripheralMeasurementEventServiceBlockingStub {
        val header = getTokenHeader()
        val stub = PeripheralMeasurementEventServiceGrpc.newBlockingStub(eventsChannel)
        return MetadataUtils.attachHeaders(stub, header)
    }

}