package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class MateriActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi)

        // Ambil referensi button dari layout
        val btnPengenalan = findViewById<Button>(R.id.btnPengenalan)
        val btnJenisAksara = findViewById<Button>(R.id.btnJenis)

        // Klik "PENGENALAN AKSARA"
        btnPengenalan.setOnClickListener {
            val intent = Intent(this, MateriPengenalanActivity::class.java)
            startActivity(intent)
        }

        // Klik "JENIS-JENIS AKSARA"
        btnJenisAksara.setOnClickListener {
            val intent = Intent(this, MateriJenisSwaraActivity::class.java)
            startActivity(intent)
        }
    }
}
