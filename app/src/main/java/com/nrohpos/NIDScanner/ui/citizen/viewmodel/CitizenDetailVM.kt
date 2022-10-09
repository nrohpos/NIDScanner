package com.nrohpos.NIDScanner.ui.citizen.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.nrohpos.NIDScanner.base.BaseView
import com.nrohpos.NIDScanner.base.BaseViewModel
import com.nrohpos.NIDScanner.model.CitizenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface CitizenDetailView : BaseView {
    fun loadCitizen(citizenModel: CitizenModel)
    fun errorData()
}

class CitizenDetailVM : BaseViewModel<CitizenDetailView>() {
    override fun bind(view: CitizenDetailView) {
        this.view = view
    }

    fun initData(bundle: Bundle?) {
        viewModelScope.launch(Dispatchers.IO) {
            val citizenModel = bundle?.get("data") as? CitizenModel
            withContext(Dispatchers.Main) {
                if (bundle == null) {
                    view?.errorData()
                } else {
                    citizenModel?.let {
                        view?.loadCitizen(it)
                    }
                }
            }
        }
    }
}