package com.example.widyaaksara.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class KuisTerjemahanActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuis_terjemahan)

        // Ambil referensi Card dari layout
        val kuisTerjemahanSundaKeLatin: ImageView = findViewById(R.id.card_kuis_terjemahan_sunda_ke_latin)
        val kuisTerjemahanLatinKeSunda: ImageView = findViewById(R.id.card_kuis_terjemahan_latin_ke_sunda)

        // Klik "TERJEMAHAN SUNDA KE LATIN"
        kuisTerjemahanSundaKeLatin.setOnClickListener {
            val intent = Intent(this, KuisTerjemahanSundaKeLatinActivity::class.java)
            startActivity(intent)
        }

        // Klik "TERJEMAHAN LATIN KE SUNDA"
        kuisTerjemahanLatinKeSunda.setOnClickListener {
            val intent = Intent(this, KuisTerjemahanLatinKeSundaActivity::class.java)
            startActivity(intent)
        }

    }
}
