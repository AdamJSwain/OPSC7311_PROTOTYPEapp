package com.example.opsc7311_prototypeapp

//imports required for the app
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.opsc7311_prototypeapp.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth


class login : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth


    //create the login function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()
        val tvUsername = findViewById<TextView>(R.id.etUsername)
        val tvPassword = findViewById<TextView>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtSignUpMethod = findViewById<TextView>(R.id.txtSignUp)

        //this will remove the action bar
        supportActionBar!!.hide()

        //the button method for when the user enters their details
        btnLogin.setOnClickListener() {
            val email = tvUsername.text.toString()
            val password = tvPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, NavigationDrawer::class.java)
                        startActivity(intent)
                        Worker.userInfo = email
                    } else {
                        Toast.makeText(this, it.exception?.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please ensure all fields are filled out", Toast.LENGTH_LONG)
                    .show()
            }
        }

        txtSignUpMethod.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
