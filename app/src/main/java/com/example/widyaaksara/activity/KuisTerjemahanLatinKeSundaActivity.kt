package com.example.widyaaksara.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
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
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private val totalTimeInMillis: Long = 30 * 60 * 1000  // 30 menit dalam milidetik

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
        timerTextView = findViewById(R.id.tvTimer)

        btnNext.setOnClickListener { nextSoal() }
        btnNext.visibility = View.GONE

        startTimer() // Mulai timer saat activity dimulai
        getKuisData()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Ketika waktu habis, tampilkan skor dan akhiri kuis
                Toast.makeText(
                    this@KuisTerjemahanLatinKeSundaActivity,
                    "Waktu habis!",
                    Toast.LENGTH_SHORT
                ).show()
                selesaiKuis()
            }
        }.start()
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
                            Toast.makeText(
                                this@KuisTerjemahanLatinKeSundaActivity,
                                "Tidak ada soal tersedia",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@KuisTerjemahanLatinKeSundaActivity,
                            "Gagal mendapatkan data!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("KuisActivity", "Response gagal: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<KuisResponse>, t: Throwable) {
                Log.e("KuisActivity", "Error: ${t.message}")
                Toast.makeText(
                    this@KuisTerjemahanLatinKeSundaActivity,
                    "Gagal terhubung ke server!",
                    Toast.LENGTH_SHORT
                ).show()
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

        val mediaPlayer: MediaPlayer
        val imageRes: Int
        val colorRes: Int

        if (jawabanUser == jawabanBenar) {
            jumlahBenar++
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_benar) // Suara benar
            imageRes = R.drawable.asset_notif_benar // Gambar jawaban benar
            colorRes = R.color.green
        } else {
            jumlahSalah++
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_salah) // Suara salah
            imageRes = R.drawable.asset_notif_salah// Gambar jawaban salah
            colorRes = R.color.red
        }

        mediaPlayer.start() // Mainkan suara
        button.setBackgroundColor(ContextCompat.getColor(this, colorRes))

        // Tampilkan dialog dengan gambar
        tampilkanDialogJawaban(imageRes)

        // Lepaskan media player setelah selesai diputar
        mediaPlayer.setOnCompletionListener { mp -> mp.release() }
    }

    private fun tampilkanDialogJawaban(imageRes: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_feedback_kuis, null)
        val dialogBuilder = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        // Atur background dialog
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.button_rounded)

        val imgJawaban = dialogView.findViewById<ImageView>(R.id.imgJawaban)
        imgJawaban.setImageResource(imageRes)

        // Atur dialog agar otomatis hilang setelah 2 detik dan lanjut ke soal berikutnya
        Handler(Looper.getMainLooper()).postDelayed({
            alertDialog.dismiss()
            nextSoal()
        }, 2000) // 2000ms = 2 detik
    }

    private fun nextSoal() {
        if (currentIndex < kuisList.size - 1) {
            currentIndex++
            tampilkanSoal()
        } else {
            selesaiKuis()
        }
    }

    private fun selesaiKuis() {
        countDownTimer.cancel() // Hentikan timer

        val skorTotal = jumlahBenar * 5
        val intent = Intent(this, SkorActivity::class.java)
        intent.putExtra("JUMLAH_BENAR", jumlahBenar)
        intent.putExtra("JUMLAH_SALAH", jumlahSalah)
        intent.putExtra("SKOR_TOTAL", skorTotal)
        startActivity(intent)
        finish()

        submitNilaiToAPI(skorTotal) // Simpan skor ke database
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
