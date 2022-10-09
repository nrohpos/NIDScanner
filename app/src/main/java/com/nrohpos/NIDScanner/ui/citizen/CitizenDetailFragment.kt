package com.nrohpos.NIDScanner.ui.citizen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.nrohpos.NIDScanner.base.BaseFragment
import com.nrohpos.NIDScanner.databinding.FragmentCitizenDetailBinding
import com.nrohpos.NIDScanner.model.CitizenModel
import com.nrohpos.NIDScanner.ui.citizen.viewmodel.CitizenDetailVM
import com.nrohpos.NIDScanner.ui.citizen.viewmodel.CitizenDetailView
import com.nrohpos.NIDScanner.utils.custom.RString
import com.nrohpos.NIDScanner.utils.extension.toEditable

class CitizenDetailFragment : BaseFragment<FragmentCitizenDetailBinding>(), CitizenDetailView {

    private val viewModel: CitizenDetailVM by viewModels()
    override fun rootView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCitizenDetailBinding {
        return FragmentCitizenDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bind(this)
        initView()
    }

    override fun loadCitizen(citizenModel: CitizenModel) {
        binding.fullNameTextView.text = citizenModel.fullName

        binding.idTypeView.inputEditText.text = "Khmer ID".toEditable()
        binding.idNumberView.hintTextView.text = getString(RString.id_number)
        binding.idNumberView.inputEditText.text = citizenModel.idNumber?.toEditable()
        binding.expireDateView.hintTextView.text = getString(RString.expire_date)
        binding.expireDateView.inputEditText.text =
            citizenModel.cardExp?.replace(".", "-")?.toEditable()

    }

    override fun errorData() {
        Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show()
    }

    //private function
    private fun initView() {
        binding.continueButton.setOnClickListener {
            (requireActivity() as? CitizenInformationActivity)?.loadSuccessFragment()
        }
        viewModel.initData(arguments)
    }


}