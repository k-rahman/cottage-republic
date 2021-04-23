package fi.oamk.cottagerepublic.ui.account

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentAccountSettingsScreenBinding


class AccountUserSettingsScreenFragment : Fragment() {

    private lateinit var binding: FragmentAccountSettingsScreenBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_account_settings_screen, container, false)

        val viewModel = ViewModelProvider(this).get(AccountUserSettingsScreenViewModel::class.java)

        viewModel.loading.observe(viewLifecycleOwner, { loading->
            loading?.let{
                if (it){
                    createSnackbar("Date fetch failed")
                }
            }
        })
        viewModel.saveStatus.observe(viewLifecycleOwner, { saveStatus->
            saveStatus?.let{
                viewModel.saveStatus.value = null
                if (it){
                    createSnackbar("Data saved")
                }
            }
        })
        viewModel.navigateToProfile.observe(viewLifecycleOwner,{
            if(it){
                findNavController().navigate(R.id.accountScreenFragment)
                viewModel.onProfileNavigated()
            }
        })

        binding.userSettingsViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }




    fun createSnackbar(msg: String){
        Snackbar.make(requireView(),msg,Snackbar.LENGTH_LONG).show()
    }

}