package com.example.opsc7311_prototypeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvUsername = findViewById<TextView>(R.id.etUsername)

        val tvPassword = findViewById<TextView>(R.id.etPassword)

        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener()
        {

            if (tvUsername.text.toString().equals("Admin") && tvPassword.text.toString().equals("Password"))
            {
                Toast.makeText(this@login, "You have successfully logged in, Welcome Admin", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, NavigationDrawer::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this@login, "Please provide a registered username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}