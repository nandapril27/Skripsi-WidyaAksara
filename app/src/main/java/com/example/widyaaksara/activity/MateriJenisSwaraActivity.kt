package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R
import com.example.widyaaksara.model.AksaraAdapter
import com.example.widyaaksara.model.AksaraItem

class MateriJenisSwaraActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_jenis_swara)

        // Inisialisasi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAksara)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // Grid dengan 4 kolom

        // Data Aksara Swara
        val aksaraList = listOf(
            AksaraItem(R.drawable.aksara_swara_a),
            AksaraItem(R.drawable.aksara_swara_e_curek),
            AksaraItem(R.drawable.aksara_swara_i),
            AksaraItem(R.drawable.aksara_swara_o),
            AksaraItem(R.drawable.aksara_swara_u),
            AksaraItem(R.drawable.aksara_swara_e),
            AksaraItem(R.drawable.aksara_swara_eu),
        )

        // Pasang adapter ke RecyclerView
        recyclerView.adapter = AksaraAdapter(aksaraList)

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
                            val intent = Intent(this@MateriJenisSwaraActivity, MateriActivity ::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                        } else {
                            // Geser ke kiri -> Buka kembali MateriJenisSwaraActivity
                            val intent = Intent(this@MateriJenisSwaraActivity, MateriJenisNgalagenaActivity::class.java)
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
                val intent = Intent(this@MateriJenisSwaraActivity, MateriActivity::class.java)
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
