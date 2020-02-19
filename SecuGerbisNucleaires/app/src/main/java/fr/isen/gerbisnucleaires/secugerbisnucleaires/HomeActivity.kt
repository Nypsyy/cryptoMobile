package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Click on book icon
        personnalInfoButton.setOnClickListener {
            newIntent(this, PersonalInfoActivity::class.java)
        }

        // Click on patient icon
        patientsInfoButton.setOnClickListener {
            newIntent(this, PatientsInfoActivity::class.java)
        }

        chatID.setOnClickListener {
            newIntent(this, MessengerActivity::class.java)
        }
    }

    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
