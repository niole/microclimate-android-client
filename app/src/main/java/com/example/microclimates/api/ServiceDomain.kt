package com.example.microclimates.api

data class ServiceDomain(val host: String, val port: Int) {
    fun url(): String {
        return "$host:$port"
    }
}
