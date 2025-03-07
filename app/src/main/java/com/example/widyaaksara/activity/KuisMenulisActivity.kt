package com.example.widyaaksara.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class KuisMenulisActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuis_menulis)

        // Ambil referensi Button dari layout
        val kuisMenulisSwara = findViewById<Button>(R.id.btnMenulisSwara)
        val kuisMenulisNgalagena1 = findViewById<Button>(R.id.btnMenulisNgalagena1)
        val kuisMenulisNgalagena2 = findViewById<Button>(R.id.btnMenulisNgalagena2)
        val kuisMenulisNgalagena3 = findViewById<Button>(R.id.btnMenulisNgalagena3)

        // Klik "KUIS MENULIS AKSARA SWARA"
        kuisMenulisSwara.setOnClickListener {
            val intent = Intent(this, KuisMenulisSwaraActivity::class.java)
            startActivity(intent)
        }

        // Klik "KUIS MENULIS AKSARA NGALAGENA"
        kuisMenulisNgalagena1.setOnClickListener {
            val intent = Intent(this, KuisMenulisNgalagenaActivity::class.java)
            startActivity(intent)
        }
        kuisMenulisNgalagena2.setOnClickListener {
            val intent = Intent(this, KuisMenulisNgalagenaActivity::class.java)
            startActivity(intent)
        }
        kuisMenulisNgalagena3.setOnClickListener {
            val intent = Intent(this, KuisMenulisNgalagenaActivity::class.java)
            startActivity(intent)
        }
    }
}
