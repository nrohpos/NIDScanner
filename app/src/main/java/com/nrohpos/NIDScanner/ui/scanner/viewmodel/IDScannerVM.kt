package com.nrohpos.NIDScanner.Viewmodel

import androidx.lifecycle.viewModelScope
import com.nrohpos.NIDScanner.base.BaseView
import com.nrohpos.NIDScanner.base.BaseViewModel
import com.nrohpos.NIDScanner.model.CitizenModel
import io.scanbot.mrzscanner.model.MRZRecognitionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IDScannerView : BaseView {
    fun onScanSuccess(citizen: CitizenModel)
}

class IDScannerVM : BaseViewModel<IDScannerView>() {
    var isStartedNextScreen: Boolean = false
    override fun bind(view: IDScannerView) {
        this.view = view
    }

    fun formatResultData(result: MRZRecognitionResult?) {
        if (result == null || isStartedNextScreen) return
        viewModelScope.launch(Dispatchers.IO) {
            isStartedNextScreen = true
            val citizen = CitizenModel(
                firstName = result.firstNameField().value,
                lastName = result.lastNameField().value,
                issuingState = result.issuingStateOrOrganizationField().value,
                nationality = result.nationalityField().value,
                dob = result.dateOfBirthField().value.replace(".", "-"),
                cardExp = result.dateOfExpiryField().value.replace(".", "-"),
                gender = result.genderField().value,
                travelDocType = result.travelDocTypeField().value,
                personalNumber = result.personalNumberField().value,
                idNumber = result.documentCodeField().value
            )
            withContext(Dispatchers.Main) {
                view?.onScanSuccess(citizen)
            }
        }
    }
}