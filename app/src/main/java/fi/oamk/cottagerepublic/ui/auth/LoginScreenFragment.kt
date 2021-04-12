package fi.oamk.cottagerepublic.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentLoginScreenBinding

class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.v("test: ", "i'm created")

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)

        val viewModel = ViewModelProvider(this).get(LoginScreenViewModel::class.java)



        viewModel.username.observe(viewLifecycleOwner, {
            Log.v("test: ", it)
            //Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.navigate.observe(viewLifecycleOwner,{
        // navigate to next screen
        // findNavController().navigate()
        // viewModel.doneNavigate()

        })
        //Gets the action from navchart and is used to naviagte
        viewModel.navigateToRegister.observe(viewLifecycleOwner,{
            if(it){
                findNavController().navigate(
                        LoginScreenFragmentDirections.actionLoginScreenFragmentToRegisterFragmentEmail()
                )
                viewModel.onRegisterNavigated()
            }
        })

        binding.loginViewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }

}