package com.nrohpos.NIDScanner.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
// why override BaseView?
// when parent is implement this the func in base is all here
// the child of base doesn't need to do so, only if they want to change so they can be do itself
open class BaseActivity : AppCompatActivity(), BaseView {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

}