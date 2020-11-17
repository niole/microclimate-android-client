package com.example.microclimates.api

import io.grpc.Channel
import io.grpc.ManagedChannelBuilder

object Channels {
    private val host = "192.168.1.161"

    fun peripheralChannel(): Channel {
        return ManagedChannelBuilder.forAddress(host, 6001).usePlaintext().build()
    }

    fun deploymentChannel(): Channel {
        return ManagedChannelBuilder.forAddress(host, 6003).usePlaintext().build()
    }

    fun userChannel(): Channel {
        return ManagedChannelBuilder.forAddress(host, 6002).usePlaintext().build()
    }

}