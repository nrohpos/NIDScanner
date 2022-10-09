package com.nrohpos.NIDScanner

import android.app.Application
import com.nrohpos.NIDScanner.utils.Constant.Companion.LICENSE_KEY
import io.scanbot.sdk.ScanbotSDKInitializer

class NIDScannerApplication : Application() {
    companion object {
        val TAG = NIDScannerApplication::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()

        ScanbotSDKInitializer()
            .prepareMRZBlobs(true)
            .license(this, LICENSE_KEY)
            .initialize(this)
    }
}