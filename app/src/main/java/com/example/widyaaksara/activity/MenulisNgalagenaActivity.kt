package com.example.widyaaksara.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.widyaaksara.R
import com.example.widyaaksara.api.ApiClient
import com.example.widyaaksara.model.Aksara
import com.example.widyaaksara.model.AksaraResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenulisNgalagenaActivity : AppCompatActivity() {
    private lateinit var btnNext: ImageButton
    private lateinit var btnBack: ImageButton
    private lateinit var imageAksara: ImageView
    private lateinit var imagePola: ImageView
    private lateinit var tvNamaAksara: TextView

    private var aksaraList: List<Aksara> = listOf()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menulis_ngalagena)

        btnNext = findViewById(R.id.btnNext)
        btnBack = findViewById(R.id.btnBack)
        imageAksara = findViewById(R.id.imageAksara)
        imagePola = findViewById(R.id.card_menulis)
        tvNamaAksara = findViewById(R.id.tvNamaAksara)

        fetchAksaraData()

        btnNext.setOnClickListener {
            if (aksaraList.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % aksaraList.size
                updateUI()
            }
        }

        btnBack.setOnClickListener {
            if (aksaraList.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) aksaraList.size - 1 else currentIndex - 1
                updateUI()
            }
        }
    }

    private fun fetchAksaraData() {
        ApiClient.instance.getAksaraNgalagena().enqueue(object : Callback<AksaraResponse> {
            override fun onResponse(call: Call<AksaraResponse>, response: Response<AksaraResponse>) {
                if (response.isSuccessful) {
                    aksaraList = response.body()?.data ?: listOf()
                    if (aksaraList.isNotEmpty()) {
                        updateUI()
                    }
                } else {
                    Toast.makeText(this@MenulisNgalagenaActivity, "Gagal mengambil data!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AksaraResponse>, t: Throwable) {
                Toast.makeText(this@MenulisNgalagenaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI() {
        val aksara = aksaraList[currentIndex]
        tvNamaAksara.text = aksara.nama

        Glide.with(this)
            .load(aksara.gambar_aksara)
            .into(imageAksara)

        Glide.with(this)
            .load(aksara.gambar_pola)
            .into(imagePola)
    }
}
