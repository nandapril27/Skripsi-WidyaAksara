package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R

class InformasiPembuatActivity : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi_pembuat)

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
                            val intent = Intent(this@InformasiPembuatActivity, InformasiBantuanActivity ::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                        }
                    }
                }
                return false
            }
        })
        // Tangani tombol back bawaan Android
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@InformasiPembuatActivity, HomeActivity::class.java)
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
