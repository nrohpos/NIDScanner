package com.nrohpos.NIDScanner.ui.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.nrohpos.NIDScanner.Viewmodel.IDScannerVM
import com.nrohpos.NIDScanner.Viewmodel.IDScannerView
import com.nrohpos.NIDScanner.base.BaseActivity

import com.nrohpos.NIDScanner.databinding.ActivityIdscannerBinding
import com.nrohpos.NIDScanner.model.CitizenModel
import com.nrohpos.NIDScanner.ui.citizen.CitizenInformationActivity
import io.scanbot.sdk.ScanbotSDK
import io.scanbot.sdk.camera.FrameHandlerResult
import io.scanbot.sdk.mrzscanner.MRZScannerFrameHandler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class IDScannerActivity : BaseActivity<ActivityIdscannerBinding>(), IDScannerView {

    companion object {
        val TAG = IDScannerActivity::class.java.simpleName

        fun startActivity(context: BaseActivity<*>) {
            Intent(context, IDScannerActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

    private val scanBotSDK: ScanbotSDK by lazy { ScanbotSDK(this) }
    private val viewModel: IDScannerVM by viewModels()
    private val citizenInformationActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.isStartedNextScreen = false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askPermission()
        viewModel.bind(this)
        initView()
    }

    //override function
    override fun rootView(): ActivityIdscannerBinding {
        return ActivityIdscannerBinding.inflate(layoutInflater)
    }

    override fun onScanSuccess(citizen: CitizenModel) {
        // go to next screen
        citizenInformationActivityResult.launch(
            Intent(this, CitizenInformationActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putSerializable("data", citizen)
                })
            }
        )
    }

    //private function
    private fun initView() {
        binding.backImageView.setOnClickListener {
            onBackPressed()
        }


        val mrzScanner = scanBotSDK.createMrzScanner()
        val mrzScannerFrameHandler = MRZScannerFrameHandler.attach(binding.cameraView, mrzScanner)
        mrzScannerFrameHandler.addResultHandler { result ->
            when (result) {
                is FrameHandlerResult.Success -> {
                    if (result.value.recognitionSuccessful) {
                        // do something with result here
                        viewModel.formatResultData(result.value)
                    }
                }
                is FrameHandlerResult.Failure -> {
                    lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(
                            this@IDScannerActivity,
                            result.error.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            false
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
}