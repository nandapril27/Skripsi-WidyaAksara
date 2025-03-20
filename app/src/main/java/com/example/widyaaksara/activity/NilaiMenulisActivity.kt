package com.example.widyaaksara.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class NilaiMenulisActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nilai_menulis)

        // Ambil referensi Button dari layout
        val nilaiMenulisSwara = findViewById<Button>(R.id.btnMenulisSwara)
        val nilaiMenulisNgalagena = findViewById<Button>(R.id.btnMenulisNgalagena)
//        val nilaiMenulisNgalagena2 = findViewById<Button>(R.id.btnMenulisNgalagena2)
//        val nilaiMenulisNgalagena3 = findViewById<Button>(R.id.btnMenulisNgalagena3)

        // Klik "KUIS MENULIS AKSARA SWARA"
        nilaiMenulisSwara.setOnClickListener {
            val intent = Intent(this, NilaiMenulisSwaraActivity::class.java)
            startActivity(intent)
        }

        // Klik "KUIS MENULIS AKSARA NGALAGENA"
        nilaiMenulisNgalagena.setOnClickListener {
            val intent = Intent(this, NilaiMenulisNgalagenaActivity::class.java)
            startActivity(intent)
        }
//        nilaiMenulisNgalagena2.setOnClickListener {
//            val intent = Intent(this, NilaiMenulisNgalagenaActivity::class.java)
//            startActivity(intent)
//        }
//        nilaiMenulisNgalagena3.setOnClickListener {
//            val intent = Intent(this, NilaiMenulisNgalagenaActivity::class.java)
//            startActivity(intent)
//        }
    }
}
