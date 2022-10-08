package com.nrohpos.NIDScanner.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T : BaseView> : ViewModel() {
    protected var view: T? = null
    abstract fun bind(view: T)

    override fun onCleared() {
        //ViewModels (VMs) are independent of configuration changes and are cleared when activity/fragment is destroyed.
        super.onCleared()
        view = null
    }
}