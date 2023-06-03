package com.example.opsc7311_prototypeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun redirectToNavigationDrawer(view: View) {
        val intent = Intent(this, NavigationDrawer::class.java)
        startActivity(intent)
    }
}