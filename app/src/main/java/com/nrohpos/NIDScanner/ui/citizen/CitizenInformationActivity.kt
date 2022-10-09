package com.nrohpos.NIDScanner.ui.citizen

import android.content.Intent
import android.os.Bundle
import com.nrohpos.NIDScanner.base.BaseActivity
import com.nrohpos.NIDScanner.databinding.ActivityCitizenInformationBinding
import com.nrohpos.NIDScanner.model.CitizenModel

class CitizenInformationActivity : BaseActivity<ActivityCitizenInformationBinding>() {

    companion object {
        val TAG = CitizenInformationActivity::class.java.simpleName
        fun startActivity(context: BaseActivity<*>, bundle: Bundle? = null) {
            Intent(context, CitizenInformationActivity::class.java).apply {
                context.startActivity(this, bundle)
            }
        }
    }

    private val citizenDetailFragment: CitizenDetailFragment by lazy { CitizenDetailFragment() }
    private val successFragment: SuccessFragment by lazy { SuccessFragment() }
    private var disableBackPress = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentDetail()
    }

    override fun onBackPressed() {
       if (disableBackPress) {
           finishAffinity()
       } else {
           setResult(RESULT_OK)
           finish()
       }
    }

    override fun rootView(): ActivityCitizenInformationBinding {
        return ActivityCitizenInformationBinding.inflate(layoutInflater)
    }

    //private function
    private fun initFragmentDetail() {
        citizenDetailFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().apply {
            add(
                binding.fragmentContainer.id,
                citizenDetailFragment,
                citizenDetailFragment.tagFragment
            )
            commit()
        }
    }

    //public function
    fun loadSuccessFragment(){
        disableBackPress = true
        supportFragmentManager.beginTransaction().apply {
            hide(citizenDetailFragment) // or you can set the background to
            // be white from the latest stack
            add(
                binding.fragmentContainer.id,
                successFragment,
                successFragment.tagFragment
            )
            commit()
        }
    }

}