package com.nrohpos.NIDScanner.ui.citizen.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.nrohpos.NIDScanner.base.BaseView
import com.nrohpos.NIDScanner.base.BaseViewModel
import com.nrohpos.NIDScanner.model.CitizenModel
import com.nrohpos.NIDScanner.utils.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

interface CitizenDetailView : BaseView {
    fun loadCitizen(citizenModel: CitizenModel)
    fun errorData()
    fun attachDatePicker(currentDate: Long?)
}

class CitizenDetailVM : BaseViewModel<CitizenDetailView>() {
    var citizenModel: CitizenModel? = null
    override fun bind(view: CitizenDetailView) {
        this.view = view
    }

    fun initData(bundle: Bundle?) {
        viewModelScope.launch(Dispatchers.IO) {
            citizenModel = bundle?.get("data") as? CitizenModel
            withContext(Dispatchers.Main) {
                if (citizenModel == null) {
                    view?.errorData()
                } else {
                    citizenModel?.let {
                        view?.loadCitizen(it)
                    }
                }
            }
        }
    }

    fun onChangeExpDate() {
        if (citizenModel != null && citizenModel!!.cardExp != null) {
            val time = DateUtil.formatDateToPicker(citizenModel!!.cardExp!!)
            view?.attachDatePicker(time)
        } else {
            view?.errorData()
        }
    }

    fun updateExpDate(time: Long) {
        citizenModel?.cardExp = DateUtil.formatPickerToDateExp(time)
        citizenModel?.let {
            view?.loadCitizen(it)
        }
    }
}