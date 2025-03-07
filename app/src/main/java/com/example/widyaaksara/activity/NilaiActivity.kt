package com.example.widyaaksara.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class NilaiActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nilai)

        // Ambil referensi Card dari layout
        val nilaiMenulis: ImageView = findViewById(R.id.card_nilai_menulis)
        val nilaiTerjemahan: ImageView = findViewById(R.id.card_nilai_terjemahan)

        // Klik "MENULIS AKSARA"
        nilaiMenulis.setOnClickListener {
            val intent = Intent(this, NilaiMenulisActivity::class.java)
            startActivity(intent)
        }

        // Klik "TERJEMAHAN"
        nilaiTerjemahan.setOnClickListener {
            val intent = Intent(this, NilaiTerjemahanActivity::class.java)
            startActivity(intent)
        }
    }
}
