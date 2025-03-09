package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.widyaaksara.R
import com.example.widyaaksara.api.ApiClient
import com.example.widyaaksara.model.KuisModel
import com.example.widyaaksara.model.KuisResponse
import com.example.widyaaksara.model.NilaiRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class KuisTerjemahanLatinKeSundaActivity : AppCompatActivity() {

    private lateinit var tvSoal: TextView
    private lateinit var btnA: ImageView
    private lateinit var btnB: ImageView
    private lateinit var btnC: ImageView
    private lateinit var btnD: ImageView
    private lateinit var btnNext: ImageView

    private var kuisList: List<KuisModel> = listOf()
    private var currentIndex = 0
    private var isAnswered = false
    private var jumlahBenar = 0
    private var jumlahSalah = 0
    private var call: Call<KuisResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuis_terjemahan_latin_ke_sunda)

        tvSoal = findViewById(R.id.tvSoal)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener { nextSoal() }
        btnNext.visibility = View.GONE

        getKuisData()
    }

    private fun getKuisData() {
        call = ApiClient.instance.getKuisLatinKeAksara()
        call?.enqueue(object : Callback<KuisResponse> {
            override fun onResponse(call: Call<KuisResponse>, response: Response<KuisResponse>) {
                if (response.isSuccessful) {
                    val kuisResponse = response.body()
                    if (kuisResponse?.success == true) {
                        // Mengacak daftar soal sebelum ditampilkan
                        kuisList = kuisResponse.data.shuffled(Random(System.currentTimeMillis()))
                        if (kuisList.isNotEmpty()) {
                            tampilkanSoal()
                        } else {
                            Toast.makeText(this@KuisTerjemahanLatinKeSundaActivity, "Tidak ada soal tersedia", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@KuisTerjemahanLatinKeSundaActivity, "Gagal mendapatkan data!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("KuisActivity", "Response gagal: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<KuisResponse>, t: Throwable) {
                Log.e("KuisActivity", "Error: ${t.message}")
                Toast.makeText(this@KuisTerjemahanLatinKeSundaActivity, "Gagal terhubung ke server!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tampilkanSoal() {
        if (currentIndex >= kuisList.size) return

        val kuis = kuisList[currentIndex]

        resetButtonColors()
        btnNext.visibility = View.GONE

        tvSoal.text = kuis.soal ?: "Soal tidak tersedia"

        loadImage(kuis.A, btnA)
        loadImage(kuis.B, btnB)
        loadImage(kuis.C, btnC)
        loadImage(kuis.D, btnD)

        isAnswered = false

        btnA.setOnClickListener { cekJawaban("A", kuis.jawaban, btnA) }
        btnB.setOnClickListener { cekJawaban("B", kuis.jawaban, btnB) }
        btnC.setOnClickListener { cekJawaban("C", kuis.jawaban, btnC) }
        btnD.setOnClickListener { cekJawaban("D", kuis.jawaban, btnD) }
    }

    private fun cekJawaban(jawabanUser: String, jawabanBenar: String, button: ImageView) {
        if (isAnswered) return

        isAnswered = true

        if (jawabanUser == jawabanBenar) {
            jumlahBenar++
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            Toast.makeText(this, "Jawaban Benar!", Toast.LENGTH_SHORT).show()
        } else {
            jumlahSalah++
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            Toast.makeText(this, "Jawaban Salah!", Toast.LENGTH_SHORT).show()
        }

        btnNext.visibility = View.VISIBLE
    }

    private fun nextSoal() {
        if (currentIndex < kuisList.size - 1) {
            currentIndex++
            tampilkanSoal()
        } else {
            val skorTotal = jumlahBenar * 5  // Kalkulasi nilai
            val intent = Intent(this, SkorActivity::class.java)
            intent.putExtra("JUMLAH_BENAR", jumlahBenar)
            intent.putExtra("JUMLAH_SALAH", jumlahSalah)
            intent.putExtra("SKOR_TOTAL", skorTotal) // Kirim skor total
            startActivity(intent)
            finish()
            submitNilaiToAPI(skorTotal)
        }
    }

    // Simpan Nilai Ke Database
    private fun submitNilaiToAPI(nilai: Int) {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val nis = sharedPref.getString("NIS", null)

        if (nis.isNullOrEmpty()) {
            Log.e("SESSION_ERROR", "NIS tidak ditemukan dalam sesi")
            return
        }

        val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val nilaiRequest = NilaiRequest(nis, nilai, tanggal) // Pastikan 'nilai' tetap Int

        ApiClient.instance.simpanNilaiLatin(nilaiRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    // Log response mentah untuk debug
                    val responseBody = response.body()?.string() ?: "Response kosong"
                    Log.d("API_RESPONSE", "Raw Response: $responseBody")

                    if (response.isSuccessful) {
                        Log.d("API_SUCCESS", "Nilai berhasil disimpan!")
                        Toast.makeText(this@KuisTerjemahanLatinKeSundaActivity, "Nilai berhasil disimpan!", Toast.LENGTH_SHORT).show()

                        // Pindah ke SkorActivity sambil membawa data jumlah benar, salah, dan skor total
                        val intent = Intent(
                            this@KuisTerjemahanLatinKeSundaActivity,
                            SkorActivity::class.java
                        )
                        intent.putExtra("JUMLAH_BENAR", jumlahBenar)
                        intent.putExtra("JUMLAH_SALAH", jumlahSalah)
                        intent.putExtra("SKOR_TOTAL", nilai)
                        startActivity(intent)
                        finish()
                    } else {
                        // Log error dengan lebih detail
                        val errorBody = response.errorBody()?.string()
                        Log.e("API_ERROR", "Gagal menyimpan nilai: $errorBody")

                        if (errorBody.isNullOrEmpty()) {
                            Log.e("API_ERROR", "Kemungkinan response dari API tidak valid atau bukan JSON")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("API_EXCEPTION", "Error parsing response: ${e.message}")
                }
        }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API_FAILURE", "Error: ${t.message}")
            }
        })
    }

    private fun resetButtonColors() {
        val defaultColor = ContextCompat.getColor(this, R.color.blue)
        btnA.setBackgroundColor(defaultColor)
        btnB.setBackgroundColor(defaultColor)
        btnC.setBackgroundColor(defaultColor)
        btnD.setBackgroundColor(defaultColor)
    }

    private fun loadImage(url: String, imageView: ImageView) {
        if (url.startsWith("http")) {
            if (!isDestroyed && !isFinishing) {
                Glide.with(this).load(url).into(imageView)
            }
        } else {
            Log.e("KuisActivity", "URL gambar tidak valid atau kosong")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }
}
