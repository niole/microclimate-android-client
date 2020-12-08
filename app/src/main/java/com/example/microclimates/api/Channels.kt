package com.example.microclimates.api

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object Channels {
    // TODO don't hardcode this
    private val host = "192.168.1.162"

    val eventServiceDomain = ServiceDomain(host, 6004)
    val peripheralServiceDomain = ServiceDomain(host, 6001)

    fun eventsChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(eventServiceDomain.host, eventServiceDomain.port).usePlaintext().build()
    }

    fun peripheralChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(peripheralServiceDomain.host, peripheralServiceDomain.port).usePlaintext().build()
    }

    fun deploymentChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(host, 6003).usePlaintext().build()
    }

    fun userChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(host, 6002).usePlaintext().build()
    }

}