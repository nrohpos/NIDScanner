package com.nrohpos.NIDScanner.Viewmodel

import android.os.Build
import androidx.biometric.BiometricManager
import com.nrohpos.NIDScanner.base.BaseView
import com.nrohpos.NIDScanner.base.BaseViewModel

interface MainView : BaseView {
    fun errorMessage(msg: String)
    fun authenticateSuccess()
    fun actionBiometricEnroll()
}

class MainActivityVM : BaseViewModel<MainView>() {
    override fun bind(view: MainView) {
        this.view = view
    }

    fun checkDeviceCapability(code: Int) {
        when (code) {
            BiometricManager.BIOMETRIC_SUCCESS, BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                view?.authenticateSuccess()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                view?.errorMessage("No biometric features available on this device")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                view?.errorMessage("Biometric features are currently unavailable")
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                view?.errorMessage("Biometric options are incompatible with the current Android version")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    view?.actionBiometricEnroll()
                } else {
                    view?.errorMessage("Could not request biometric enrollment in Android Version smaller than 10")
                }
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
               view?.errorMessage( "Biometric features are unavailable because security vulnerabilities has been discovered in one or more hardware sensors")
            }
            else -> {
                view?.errorMessage("Something went wrong!")
                throw IllegalStateException()
            }
        }
    }

}