package fi.oamk.cottagerepublic

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText


class RegisterFragmentEmail : Fragment(),View.OnClickListener {
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_email, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.navigate_button_email).setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        val email = view?.findViewById<EditText>(R.id.userEmail)?.text.toString()
        print(email)
        when(v!!.id){
            R.id.navigate_button_email ->{
                if (!TextUtils.isEmpty(email)){
                    val bundle = bundleOf("email" to email)
                    navController.navigate(R.id.action_registerFragmentName_to_registerFragmentPassword,bundle)
                }
            }
        }
    }
}