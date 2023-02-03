package com.example.weather_radar_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var  btnUser: Button
    private lateinit var  btnAdmin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        btnUser = findViewById(R.id.buttonUser)
        btnAdmin = findViewById(R.id.buttonAdmin)
        btnAdmin.setOnClickListener{
            val intent= Intent(this,Admin::class.java)
            startActivity(intent)
        }
        btnUser.setOnClickListener{
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }

}