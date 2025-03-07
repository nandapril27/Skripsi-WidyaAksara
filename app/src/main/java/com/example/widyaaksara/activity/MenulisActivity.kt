package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class MenulisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menulis)

        // Ambil referensi button dari layout
        val MenulisSwara = findViewById<Button>(R.id.MenulisSwara)
        val MenulisNgalagena = findViewById<Button>(R.id.MenulisNgalagena)

        // Klik "AKSARA SWARA"
        MenulisSwara.setOnClickListener {
            val intent = Intent(this, MenulisSwaraActivity::class.java)
            startActivity(intent)
        }

        // Klik "AKSARA NGALAGENA"
        MenulisNgalagena.setOnClickListener {
            val intent = Intent(this, MenulisNgalagenaActivity::class.java)
            startActivity(intent)
        }
    }
}
