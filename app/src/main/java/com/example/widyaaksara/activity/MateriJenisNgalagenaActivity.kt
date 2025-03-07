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

class MateriJenisNgalagenaActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_jenis_ngalagena)

        // Inisialisasi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAksara)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // Grid dengan 4 kolom

        // Data Aksara Swara
        val aksaraList = listOf(
            AksaraItem(R.drawable.aksara_ngalagena_ka),
            AksaraItem(R.drawable.aksara_ngalagena_ga),
            AksaraItem(R.drawable.aksara_ngalagena_nga),
            AksaraItem(R.drawable.aksara_ngalagena_ca),
            AksaraItem(R.drawable.aksara_ngalagena_ja),
            AksaraItem(R.drawable.aksara_ngalagena_nya),
            AksaraItem(R.drawable.aksara_ngalagena_ta),
            AksaraItem(R.drawable.aksara_ngalagena_da),
            AksaraItem(R.drawable.aksara_ngalagena_na),
            AksaraItem(R.drawable.aksara_ngalagena_pa),
            AksaraItem(R.drawable.aksara_ngalagena_ba),
            AksaraItem(R.drawable.aksara_ngalagena_ma),
            AksaraItem(R.drawable.aksara_ngalagena_ya),
            AksaraItem(R.drawable.aksara_ngalagena_ra),
            AksaraItem(R.drawable.aksara_ngalagena_la),
            AksaraItem(R.drawable.aksara_ngalagena_wa),
            AksaraItem(R.drawable.aksara_ngalagena_sa),
            AksaraItem(R.drawable.aksara_ngalagena_ha),
            AksaraItem(R.drawable.aksara_ngalagena_fa),
            AksaraItem(R.drawable.aksara_ngalagena_va),
            AksaraItem(R.drawable.aksara_ngalagena_qa),
            AksaraItem(R.drawable.aksara_ngalagena_xa),
            AksaraItem(R.drawable.aksara_ngalagena_za),
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
                            val intent = Intent(this@MateriJenisNgalagenaActivity, MateriJenisSwaraActivity ::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            // Geser ke kiri -> Buka kembali MateriJenisSwaraActivity
                            val intent = Intent(this@MateriJenisNgalagenaActivity, MateriJenisAngkaActivity::class.java)
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