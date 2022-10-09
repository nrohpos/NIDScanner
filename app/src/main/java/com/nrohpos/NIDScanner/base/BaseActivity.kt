package com.nrohpos.NIDScanner.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

// why override BaseView?
// when parent is implement this the func in base is all here
// the child of base doesn't need to do so, only if they want to change so they can be do itself
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), BaseView {
    abstract fun rootView(): VB
    protected lateinit var binding: VB

    @SuppressLint("SourceLockedOrientationActivity")
    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = rootView()
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

}