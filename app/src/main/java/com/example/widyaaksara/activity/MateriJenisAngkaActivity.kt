package com.example.widyaaksara.activity


import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R
import com.example.widyaaksara.model.AksaraAdapter
import com.example.widyaaksara.model.AksaraItem

class MateriJenisAngkaActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_jenis_angka)

        // Inisialisasi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAksara)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // Grid dengan 4 kolom

        // Data Aksara Swara
        val aksaraList = listOf(
            AksaraItem(R.drawable.aksara_angka_1),
            AksaraItem(R.drawable.aksara_angka_2),
            AksaraItem(R.drawable.aksara_angka_3),
            AksaraItem(R.drawable.aksara_angka_4),
            AksaraItem(R.drawable.aksara_angka_5),
            AksaraItem(R.drawable.aksara_angka_6),
            AksaraItem(R.drawable.aksara_angka_7),
            AksaraItem(R.drawable.aksara_angka_8),
            AksaraItem(R.drawable.aksara_angka_9),
            AksaraItem(R.drawable.aksara_angka_0),
        )

        // Pasang adapter ke RecyclerView
        recyclerView.adapter = AksaraAdapter(aksaraList)


        // Inisialisasi GestureDetector
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
                            val intent = Intent(this@MateriJenisAngkaActivity, MateriJenisNgalagenaActivity ::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            // Geser ke kiri -> Buka kembali MateriJenisSwaraActivity
                            val intent = Intent(this@MateriJenisAngkaActivity, MateriJenisRarangkenActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                        }
                        return true
                    }
                }
                return false
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