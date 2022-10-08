package com.nrohpos.NIDScanner

import android.app.Activity
import android.content.Intent
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
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nrohpos.NIDScanner.Viewmodel.MainActivityVM
import com.nrohpos.NIDScanner.Viewmodel.MainView
import com.nrohpos.NIDScanner.base.BaseActivity
import com.nrohpos.NIDScanner.databinding.ActivityMainBinding
import java.util.concurrent.Executor

class MainActivity : BaseActivity(), MainView {

    companion object {
        val TAG = MainActivity::class.simpleName
    }

    private val viewModel: MainActivityVM by viewModels()
    private lateinit var binding: ActivityMainBinding

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.bind(this)
        initView()
    }

    private fun initView() {
        binding.cameraLayout.setOnClickListener {
            tryAuthenticateBiometric()
        }
        binding.continueTextView.setOnClickListener {
            tryAuthenticateBiometric()
        }
        initBiometric()
    }

    //Override function
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
            val type = result.authenticationType
            showSnackBar("\uD83C\uDF89 Authentication successful! Type: $type \uD83C\uDF89")
        }
    }

    private fun initBiometric() {
        // Initialize PromptInfo for title, subtitle, and authenticators of the biometric
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

    private fun tryAuthenticateBiometric() {
        viewModel.checkDeviceCapability(biometricManager.canAuthenticate(authenticators))
    }

    private fun showSnackBar(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }
    // any public function here
}