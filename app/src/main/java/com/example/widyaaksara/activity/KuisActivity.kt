package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class KuisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuis)

        // Ambil referensi Card dari layout
        val kuisMenulis: ImageView = findViewById(R.id.card_kuis_menulis)
        val kuisTerjemahan: ImageView = findViewById(R.id.card_kuis_terjemahan)

        // Klik "MENULIS AKSARA"
        kuisMenulis.setOnClickListener {
            val intent = Intent(this, KuisMenulisActivity::class.java)
            startActivity(intent)
        }

        // Klik "TERJEMAHAN"
        kuisTerjemahan.setOnClickListener {
            val intent = Intent(this, KuisTerjemahanActivity::class.java)
            startActivity(intent)
        }

    }
}
