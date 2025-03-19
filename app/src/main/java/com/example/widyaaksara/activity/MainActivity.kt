package com.example.widyaaksara.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R
import com.example.widyaaksara.service.BacksoundService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, BacksoundService::class.java)) // Mulai backsound

        val sharedPreferences: SharedPreferences = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false)

        // Delay untuk Splash Screen
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish() // Menutup MainActivity agar tidak kembali ke splash screen
        }, 3000) // 3000 ms = 3 detik

    }
}