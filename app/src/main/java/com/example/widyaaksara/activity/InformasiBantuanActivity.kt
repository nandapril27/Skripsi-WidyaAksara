package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R
import com.example.widyaaksara.model.BantuanAdapter
import com.example.widyaaksara.model.Bantuan

class InformasiBantuanActivity : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi_bantuan)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Data bantuan
        val listBantuan = listOf(
            Bantuan(R.drawable.asset_card_menu_materi, "MATERI", "Nyayogikeun pamekar literasi ngeunaan aksara Sunda jeung rupa-rupa wangun dasar aksara Sunda."),
            Bantuan(R.drawable.asset_card_menu_menulis, "MENULIS", "Fitur pikeun latihan nulis aksara Sunda maké widang tulis interaktif jeung panduan penulisan. Sarta nerapkeun validasi ngagunakeun algoritma PIP pikeun nalungtik katepatan pola tulisan."),
            Bantuan(R.drawable.asset_card_menu_kuis, "KUIS", "Kuis Menulis, pikeun ngalatih katepatan tulisan aksara sunda.\nKuis Terjemahan, nyaeta soal PG pikeun nguji kamampuh maca jeung ngartikeun aksara Sunda."),
            Bantuan(R.drawable.asset_card_menu_nilai, "NILAI", "Nampilkeun  riwayat nilai kuis sanggeus réngsé latihan, kaasup skor jeung waktu"),
            Bantuan(R.drawable.asset_card_menu_informasi, "INFORMASI", "Nampilkeun informasi ngeunaan modul ajar, menu dina aplikasi jeung profil nu ngadamel aplikasi."),
            Bantuan(R.drawable.asset_card_menu_profil, "PROFIL", "Nampilkeun data siswa, saperti NIS jeung Nama.")
        )

        // Atur RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BantuanAdapter(listBantuan)

        // Inisialisasi GestureDetector untuk swipe
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 != null && e2 != null) {
                    val deltaX = e2.x - e1.x
                    val threshold = 100 // Jarak minimal untuk swipe
                    val velocityThreshold = 100 // Kecepatan minimal untuk swipe

                    if (Math.abs(deltaX) > threshold && Math.abs(velocityX) > velocityThreshold) {
                        if (deltaX > 0) {
                            // Geser ke kanan -> Buka MateriJenisNgalagenaActivity
                            val intent = Intent(this@InformasiBantuanActivity, InformasiActivity ::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                        } else {
                            // Geser ke kiri -> Buka kembali MateriJenisSwaraActivity
                            val intent = Intent(this@InformasiBantuanActivity,InformasiPembuatActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                        return true
                    }
                }
                return false
            }
        })

        // Tangani tombol back bawaan Android
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@InformasiBantuanActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish() // Tutup activity ini
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (event?.let { gestureDetector.onTouchEvent(it) } == true) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }
}

