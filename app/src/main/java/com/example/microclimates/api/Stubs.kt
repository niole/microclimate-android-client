package com.example.microclimates.api

import api.DeploymentManagementServiceGrpc
import api.PeripheralManagementServiceGrpc
import api.UserServiceGrpc

object Stubs {
    fun peripheralStub(): PeripheralManagementServiceGrpc.PeripheralManagementServiceBlockingStub {
        return PeripheralManagementServiceGrpc.newBlockingStub(Channels.peripheralChannel())
    }

    fun blockingDeploymentStub(): DeploymentManagementServiceGrpc.DeploymentManagementServiceBlockingStub {
        return DeploymentManagementServiceGrpc.newBlockingStub(Channels.deploymentChannel())
    }

    fun userStub(): UserServiceGrpc.UserServiceFutureStub {
        return UserServiceGrpc.newFutureStub(Channels.userChannel())
    }

}