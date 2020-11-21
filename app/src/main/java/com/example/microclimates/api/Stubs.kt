package com.example.microclimates.api

import api.DeploymentManagementServiceGrpc
import api.PeripheralManagementServiceGrpc
import api.PeripheralMeasurementEventServiceGrpc
import api.UserServiceGrpc
import io.grpc.ManagedChannel

object Stubs {
    fun peripheralStub(peripheralChannel: ManagedChannel): PeripheralManagementServiceGrpc.PeripheralManagementServiceBlockingStub {
        return PeripheralManagementServiceGrpc.newBlockingStub(peripheralChannel)
    }

    fun blockingDeploymentStub(deploymentChannel: ManagedChannel): DeploymentManagementServiceGrpc.DeploymentManagementServiceBlockingStub {
        return DeploymentManagementServiceGrpc.newBlockingStub(deploymentChannel)
    }

    fun userStub(userChannel: ManagedChannel): UserServiceGrpc.UserServiceFutureStub {
        return UserServiceGrpc.newFutureStub(userChannel)
    }

    fun eventsStub(eventsChannel: ManagedChannel): PeripheralMeasurementEventServiceGrpc.PeripheralMeasurementEventServiceBlockingStub {
        return PeripheralMeasurementEventServiceGrpc.newBlockingStub(eventsChannel)
    }

}