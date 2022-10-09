package com.nrohpos.NIDScanner.ui.citizen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nrohpos.NIDScanner.base.BaseFragment
import com.nrohpos.NIDScanner.databinding.FragmentSuccessBinding
import com.nrohpos.NIDScanner.ui.main.MainActivity


class SuccessFragment : BaseFragment<FragmentSuccessBinding>() {

    override fun rootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSuccessBinding {
        return FragmentSuccessBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeButton.setOnClickListener {
            Intent(
                requireActivity(),
                MainActivity::class.java
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }
}