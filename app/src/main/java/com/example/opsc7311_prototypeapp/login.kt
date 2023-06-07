package com.example.opsc7311_prototypeapp

//imports required for the app
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class login : AppCompatActivity() {

    //create the login function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvUsername = findViewById<TextView>(R.id.etUsername)
        val tvPassword = findViewById<TextView>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        //this will remove the action bar
        supportActionBar!!.hide()

        //the button method for when the user enters their details
        btnLogin.setOnClickListener()
        {
            if (tvUsername.text.toString().equals("Wilhelm") && tvPassword.text.toString().equals("Boemaloem"))
            {
                //will pass a message that the user must register or say login successful
                Toast.makeText(this@login, "You have successfully logged in, Welcome, "+tvUsername.text.toString(), Toast.LENGTH_SHORT).show()
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