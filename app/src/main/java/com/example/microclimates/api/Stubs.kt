package com.example.microclimates.api

import api.DeploymentManagementServiceGrpc
import api.UserServiceGrpc

object Stubs {
    fun blockingDeploymentStub(): DeploymentManagementServiceGrpc.DeploymentManagementServiceBlockingStub {
        return DeploymentManagementServiceGrpc.newBlockingStub(Channels.deploymentChannel())
    }

    fun userStub(): UserServiceGrpc.UserServiceFutureStub {
        return UserServiceGrpc.newFutureStub(Channels.userChannel())
    }

}