package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    val email = emailSignUpEdit.text.toString()
    val password = passwordSignUpEdit.text.toString()
    val firstname = firstnameSignUpEdit.text.toString()
    val lastname = lastnameSignUpEdit.text.toString()
    val phone = phoneSignUpEdit.text.toString()
    val adminCode = codeAdminEdit.text.toString()

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        userDatabase = FirebaseDatabase.getInstance().reference

        buttonsignup.setOnClickListener{
            registerUser()
        }
    }

    private fun registerUser() {

        val database = FirebaseDatabase.getInstance()
        var ref = database.getReference("Code_Admin")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val realAdminCodeDB = dataSnapshot.getValue()!!
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })


        if( email.isEmpty()     ||
            password.isEmpty()  ||
            firstname.isEmpty() ||
            lastname.isEmpty()  ||
            phone.isEmpty()     ||
            adminCode.isEmpty()){
            Toast.makeText(this,"You should fill everything ! ", Toast.LENGTH_LONG).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email ", Toast.LENGTH_LONG).show()
        }
        else if (password.length < 12){
            Toast.makeText(this,"Password should be longer than 12 characters ", Toast.LENGTH_LONG).show()
        }
        else if (!Patterns.PHONE.matcher(phone).matches()){
            Toast.makeText(this,"Invalid phone number ", Toast.LENGTH_LONG).show()
        }

        else if (!adminCode.equals("1234")){
            Toast.makeText(this,"Invalid Admin Code ", Toast.LENGTH_LONG).show()
        }
        else{
            createAccount(email,password)
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }


    private fun createAccount(email: String, password: String) {

        var map = mutableMapOf<String, Any>()
        map["firstname"] = firstname
        map["lastname"] = lastname
        map["phone"] = phone
        map["email"] = email
        map["password"] = password

        val userID = FirebaseDatabase.getInstance().reference.child("Nurse").push().key.toString()

        FirebaseDatabase.getInstance().reference.child("Nurse").child(userID).setValue(map).addOnCompleteListener {
            Toast.makeText(this, "Registered", Toast.LENGTH_LONG).show()
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    sendEmailVerification()
                    updateUI(user)
                    goToLogin()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun sendEmailVerification(){

        val user = mAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            Toast.makeText(this,"You already have an account",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"You don't have account",Toast.LENGTH_LONG).show();
        }
    }

    private fun goToLogin() {
        val homeIntent = Intent(
            this,
            LoginActivity::class.java
        )
        startActivity(homeIntent)
    }
}