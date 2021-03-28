package fi.oamk.cottagerepublic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fi.oamk.cottagerepublic.databinding.RegisterScreenBinding


class RegisterScreen : AppCompatActivity() {

    private lateinit var binding: RegisterScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = RegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}