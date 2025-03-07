package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R
import com.example.widyaaksara.model.MenuAdapter

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Ambil referensi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Nama menu sesuai urutan dalam grid
        val menuNames = listOf("MATERI", "MENULIS", "KUIS", "NILAI", "INFORMASI", "PROFIL")

        // Set GridLayoutManager dengan 2 kolom
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Set Adapter untuk RecyclerView
        recyclerView.adapter = MenuAdapter(menuNames) { menuName ->
            when (menuName) {
                "MATERI" -> {
                    val intent = Intent(this, MateriActivity::class.java)
                    startActivity(intent)
                }
                "MENULIS" -> {
                    val intent = Intent(this, MenulisActivity::class.java)
                    startActivity(intent)
                }
                "KUIS" -> {
                    val intent = Intent(this, KuisActivity::class.java)
                    startActivity(intent)
                }
                "NILAI" -> {
                    val intent = Intent(this, NilaiActivity::class.java)
                    startActivity(intent)
                }
                "INFORMASI" -> {
                    val intent = Intent(this, InformasiActivity::class.java)
                    startActivity(intent)
                }
                "PROFIL" -> {
                    val intent = Intent(this, ProfilActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "$menuName clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
