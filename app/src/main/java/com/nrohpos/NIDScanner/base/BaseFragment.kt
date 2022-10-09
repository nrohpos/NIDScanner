package com.nrohpos.NIDScanner.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.nrohpos.NIDScanner.databinding.FragmentSuccessBinding
import java.util.UUID

abstract class BaseFragment<VB : ViewBinding> : Fragment(), BaseView {
    private var _binding: VB? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    protected val binding get() = _binding!!
    val tagFragment = UUID.randomUUID().toString()
    abstract fun rootView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = rootView(inflater, container, savedInstanceState)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}