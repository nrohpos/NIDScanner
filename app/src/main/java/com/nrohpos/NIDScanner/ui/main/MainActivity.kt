package com.nrohpos.NIDScanner.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nrohpos.NIDScanner.Viewmodel.MainActivityVM
import com.nrohpos.NIDScanner.Viewmodel.MainView
import com.nrohpos.NIDScanner.base.BaseActivity
import com.nrohpos.NIDScanner.databinding.ActivityMainBinding
import com.nrohpos.NIDScanner.ui.scanner.IDScannerActivity
import java.util.concurrent.Executor

/*Android 7.1 Nougat	2.9%
Android 8.0 Oreo		4.0%
Android 8.1 Oreo		9.7%
Android 9 Pie		    18.2%
Android 10 Q		    26.5%
Android 11 R		    24.3%
Android 12 Snow cone	31	-*/
class MainActivity : BaseActivity<ActivityMainBinding>(), MainView {

    companion object {
        val TAG = MainActivity::class.simpleName
    }

    private val viewModel: MainActivityVM by viewModels()

    // why call lazy?
    // most of this action on case is call when click
    // we dont need to Initialize value on ram before the action
    private val biometricManager: BiometricManager by lazy { BiometricManager.from(this) }
    private val executor: Executor by lazy { ContextCompat.getMainExecutor(this) }
    private val enrollBiometricRequestLauncher: ActivityResultLauncher<Intent> by lazy {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                tryAuthenticateBiometric()
            } else {
                showSnackBar("Failed to enroll in biometric")
            }
        }
    }
    private val biometricPrompt: BiometricPrompt by lazy {
        BiometricPrompt(
            this, executor,
            authenticationCallBack
        )
    }
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.bind(this)
        initView()
    }

    //Override function

    override fun rootView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun errorMessage(msg: String) {
        showSnackBar(msg)
    }

    override fun authenticateSuccess() {
        biometricPrompt.authenticate(promptInfo)
    }

    @RequiresApi(Build.VERSION_CODES.R) // already check before call back
    override fun actionBiometricEnroll() {
        enrollBiometricRequestLauncher.launch(
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    authenticators
                )
            }
        )
    }

    // private function
    private fun initView() {
        binding.cameraLayout.setOnClickListener {
            tryAuthenticateBiometric()
        }
        binding.continueTextView.setOnClickListener {
            tryAuthenticateBiometric()
        }
        initBiometric()
    }

    private val authenticationCallBack = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            showSnackBar("Authentication Error: $errString")
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            showSnackBar("Failed to authenticate. Please try again.")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            goToScanActivity()
        }
    }

    private fun initBiometric() {
        if (canDoBiometric()) {
            try {
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric authentication")
                    .setSubtitle("Please authenticate yourself first.")
                    .setAllowedAuthenticators(authenticators)
                    .build()
            } catch (e: Exception) {
                showSnackBar(e.message ?: "Unable to initialize PromptInfo")
            }
        }
    }

    private fun tryAuthenticateBiometric() {
        if (canDoBiometric()) {
            viewModel.checkDeviceCapability(biometricManager.canAuthenticate(authenticators))
        } else {
            goToScanActivity()
        }
    }

    private fun canDoBiometric(): Boolean {
        // if we don't allowed use to be scan
        // that is not user friendly
        // they all just want to use the app
        //we can add custom page for passcode for them,
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)

    }

    private fun goToScanActivity() {
        if (askPermission()) {
            IDScannerActivity.startActivity(this@MainActivity)
        }
    }

    private fun askPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 999)
            return false
        }
        return true
    }

    private fun showSnackBar(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }


}