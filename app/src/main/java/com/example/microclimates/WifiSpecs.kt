package com.example.microclimates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class WifiSpecs(val ssid: String, val password: String?) : Parcelable
