package com.example.opsc7311_prototypeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.opsc7311_prototypeapp.databinding.ActivityLoginBinding
import com.example.opsc7311_prototypeapp.databinding.ActivityMainBinding
import com.example.opsc7311_prototypeapp.databinding.FragmentHomeBinding
import com.example.opsc7311_prototypeapp.ui.categories.CategoriesFragment
import com.example.opsc7311_prototypeapp.ui.entries.EntriesFragment
import com.google.firebase.auth.FirebaseAuth
import java.net.PasswordAuthentication

class MainActivity : AppCompatActivity(){


    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //this will remove the action bar
        supportActionBar!!.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        val txtEmail = findViewById<EditText>(R.id.editTextEmail)

        val txtPassword = findViewById<EditText>(R.id.editTextPassword)

        val txtConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

        val btnSignUp = findViewById<Button>(R.id.buttonSignUp)

        val txtLogin = findViewById<TextView>(R.id.textViewLogin)

        txtLogin.setOnClickListener() {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener() {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            val confirmPassword = txtConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (Worker.isValidPassword(password.toString())) {
                    if (password == confirmPassword) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val intent = Intent(this, login::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this,
                                        it.exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Password too weak", Toast.LENGTH_SHORT).show()
                    Toast.makeText(
                        this,
                        "Password requires 8 characters or more, at least 1 number, and at least 1 capital letter",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Please ensure all fields are filled out", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

   /* override fun onDataUpdated(data: ArrayList<String>) {
        val spinnerFragment = supportFragmentManager.findFragmentByTag("EntriesFragment") as? EntriesFragment
        spinnerFragment?.updateData(data)
    }*/
}