package fi.oamk.cottagerepublic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_account)
    }

    fun userSettings(view: View) {
        Toast.makeText(this, "Go to User Settings", Toast.LENGTH_SHORT).show()
    }

    fun myCottages(view: View) {
        Toast.makeText(this, "Go to My Cottages", Toast.LENGTH_SHORT).show()
    }

    fun myReservation(view: View) {
        Toast.makeText(this, "Go to My Reservation", Toast.LENGTH_SHORT).show()
    }

    fun logout(view: View) {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
    }
}