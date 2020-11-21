package com.example.microclimates.api

import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object Channels {
    private val host = "192.168.1.162"

    fun eventsChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(host, 6004).usePlaintext().build()
    }

    fun peripheralChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(host, 6001).usePlaintext().build()
    }

    fun deploymentChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(host, 6003).usePlaintext().build()
    }

    fun userChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(host, 6002).usePlaintext().build()
    }

}