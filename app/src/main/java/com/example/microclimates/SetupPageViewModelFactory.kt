package com.example.microclimates

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class SetupPageViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }
}