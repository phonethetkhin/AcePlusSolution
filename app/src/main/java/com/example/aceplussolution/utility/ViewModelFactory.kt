package com.example.aceplussolution.utility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DirectDI
import org.kodein.di.instanceOrNull

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is the ViewModel factory to use with the ViewModel
 */
class ViewModelFactory(private val direct: DirectDI) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return direct.instanceOrNull<ViewModel>(modelClass.simpleName) as T?
            ?: modelClass.newInstance()
    }
}