package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        buttonLogin.setOnClickListener {
            doLogin()
        }

        buttonLogout.setOnClickListener {
            doLogout()
        }

        textbuttonsignin.setOnClickListener {
            goToSignUp()
        }
    }

    private fun doLogout() {
        mAuth.signOut()
        Toast.makeText(this, "Deconnected", Toast.LENGTH_LONG).show()
    }

    private fun doLogin() {
        val email = UserEdit.text.toString()
        val password = PasswordEdit.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    updateUI(user)
                    goToHome()
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }
            }
    }

    private fun goToHome() {
        val homeIntent = Intent(
            this,
            HomeActivity::class.java
        )
        startActivity(homeIntent)
    }

    private fun goToSignUp() {
        val signUpIntent = Intent(
            this,
            SignUpActivity::class.java
        )
        startActivity(signUpIntent)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
        }
    }
}