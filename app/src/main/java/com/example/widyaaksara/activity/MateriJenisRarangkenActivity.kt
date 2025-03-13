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

class MateriJenisRarangkenActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_jenis_rarangken)

        // Inisialisasi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAksara)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // Grid dengan 4 kolom

        // Data Aksara Swara
        val aksaraList = listOf(
            AksaraItem(R.drawable.aksara_rarangken_panyuku),
            AksaraItem(R.drawable.aksara_rarangken_paneleng),
            AksaraItem(R.drawable.aksara_rarangken_panghulu),
            AksaraItem(R.drawable.aksara_rarangken_pamepet),
            AksaraItem(R.drawable.aksara_rarangken_paneuleung),
            AksaraItem(R.drawable.aksara_rarangken_panolong),
            AksaraItem(R.drawable.aksara_rarangken_pangwisad),
            AksaraItem(R.drawable.aksara_rarangken_panglayar),
            AksaraItem(R.drawable.aksara_rarangken_panyecek),
            AksaraItem(R.drawable.aksara_rarangken_pamingkal),
            AksaraItem(R.drawable.aksara_rarangken_panyiku),
            AksaraItem(R.drawable.aksara_rarangken_panyakra),
            AksaraItem(R.drawable.aksara_rarangken_pamaeh),
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
                            val intent = Intent(this@MateriJenisRarangkenActivity, MateriJenisAngkaActivity ::class.java)
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
                val intent = Intent(this@MateriJenisRarangkenActivity, MateriActivity::class.java)
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