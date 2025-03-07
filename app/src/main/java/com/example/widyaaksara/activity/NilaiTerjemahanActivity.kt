package com.example.widyaaksara.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class NilaiTerjemahanActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_nilai_terjemahan)
        // Ambil referensi Card dari layout
        val nilaiTerjemahanSundaKeLatin: ImageView = findViewById(R.id.card_nilai_terjemahan_sunda_ke_latin)
        val nilaiTerjemahanLatinKeSunda: ImageView = findViewById(R.id.card_nilai_terjemahan_latin_ke_sunda)

        // Klik "TERJEMAHAN SUNDA KE LATIN"
        nilaiTerjemahanSundaKeLatin.setOnClickListener {
            val intent = Intent(this, NilaiTerjemahanSundaKeLatinActivity::class.java)
            startActivity(intent)
        }

        // Klik "TERJEMAHAN LATIN KE SUNDA"
        nilaiTerjemahanLatinKeSunda.setOnClickListener {
            val intent = Intent(this, NilaiTerjemahanLatinKeSundaActivity::class.java)
            startActivity(intent)
        }

    }
}
