package com.example.microclimates.api

import android.content.Context
import com.example.microclimates.BuildConfig
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder


// TODO remove singleton-ness and class-ness
class Channels(context: Context) {

    companion object {
        val apiPort = BuildConfig.API_PORT
        val userHost = BuildConfig.USERS_API_HOST
        val deploymentHost = BuildConfig.DEPLOYMENT_API_HOST
        val eventServiceDomain = ServiceDomain(BuildConfig.EVENTS_API_HOST, apiPort)
        val peripheralServiceDomain = ServiceDomain(BuildConfig.PERIPHERALS_API_HOST, apiPort)

        private var instance: Channels? = null

        fun getInstance(context: Context): Channels {
            if (instance == null) {
                synchronized(Channels::class.java) {
                    if (instance == null) {
                        instance = Channels(context)
                    }
                }
            }
            return instance!!
        }

    }

    fun eventsChannel(): ManagedChannel {
        return ManagedChannelBuilder
            .forAddress(
                eventServiceDomain.host,
                eventServiceDomain.port
            )
            .build()
    }

    fun peripheralChannel(): ManagedChannel {
        return ManagedChannelBuilder
            .forAddress(
                peripheralServiceDomain.host,
                peripheralServiceDomain.port
            )
            .build()
    }

    fun deploymentChannel(): ManagedChannel {
        return ManagedChannelBuilder
            .forAddress(deploymentHost, apiPort)
            .build()
    }

    fun userChannel(): ManagedChannel {
        return ManagedChannelBuilder
            .forAddress(userHost, apiPort)
            .build()
    }

}