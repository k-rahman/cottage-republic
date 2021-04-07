package fi.oamk.cottagerepublic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import fi.oamk.cottagerepublic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // due to this bug https://issuetracker.google.com/issues/142847973, https://issuetracker.google.com/issues/143145612,
        // we have to get navController from navHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeScreenFragment, R.id.accountScreenFragment, R.id.cottagesScreenFragment
        ))
        supportActionBar?.hide()
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavView.setupWithNavController(navController)
    }

    fun addCottage(view: View) {
        Toast.makeText(this, "Add new cottage", Toast.LENGTH_SHORT).show()
    }

}