package com.example.widyaaksara.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class ProfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        // Tampilkan data di dalam CardView
        val tvNIS = findViewById<TextView>(R.id.tvNIS)
        val tvNama = findViewById<TextView>(R.id.tvNama)

        val btnLogout: Button = findViewById(R.id.btnLogout)

        // Ambil data dari SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val nis: String? = sharedPreferences.getString("NIS", "Tidak ditemukan")
        val nama: String? = sharedPreferences.getString("NAMA", "Tidak ditemukan")

        // Tampilkan data di TextView
        tvNIS.text = "NIS     : $nis"
        tvNama.text = "Nama : $nama"

        // Tombol Logout
        btnLogout.setOnClickListener {
            // Hapus data dan kembali ke LoginActivity
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
