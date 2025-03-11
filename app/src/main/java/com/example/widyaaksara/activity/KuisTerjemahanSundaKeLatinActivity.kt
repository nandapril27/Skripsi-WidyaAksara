package com.example.widyaaksara.activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.os.CountDownTimer
import android.widget.TextView
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
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

class KuisTerjemahanSundaKeLatinActivity : AppCompatActivity() {

    private lateinit var soalImage: ImageView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var btnNext: ImageView
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private val totalTimeInMillis: Long = 30 * 60 * 1000  // 30 menit dalam milidetik


    private var kuisList: List<KuisModel> = listOf()
    private var currentIndex = 0
    private var isAnswered = false
    private var skorBenar = 0
    private var skorSalah = 0

    private lateinit var sharedPreferences: SharedPreferences
    private var nis: String? = null
    private var kuisId: Int = 0  // Tambahkan kuisId untuk menyimpan ke database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuis_terjemahan_sunda_ke_latin)

        soalImage = findViewById(R.id.soalImage)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnNext = findViewById(R.id.btnNext)
        timerTextView = findViewById(R.id.tvTimer)

        btnNext.setOnClickListener { nextSoal() }
        btnNext.visibility = View.GONE

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        nis = sharedPreferences.getString("NIS", null)

        if (nis.isNullOrEmpty()) {
            Toast.makeText(this, "Gagal mendapatkan data pengguna!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

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
                Toast.makeText(this@KuisTerjemahanSundaKeLatinActivity, "Waktu habis!", Toast.LENGTH_SHORT).show()
                selesaiKuis()
            }
        }.start()
    }

    private fun getKuisData() {
        ApiClient.instance.getKuis().enqueue(object : Callback<KuisResponse> {
            override fun onResponse(call: Call<KuisResponse>, response: Response<KuisResponse>) {
                if (response.isSuccessful) {
                    val kuisResponse = response.body()
                    if (kuisResponse?.success == true) {
                        // Mengacak daftar soal sebelum ditampilkan
                        kuisList = kuisResponse.data.shuffled(Random(System.currentTimeMillis()))
                        if (kuisList.isNotEmpty()) {
                            kuisId = kuisList[0].id
                            tampilkanSoal()
                        } else {
                            Toast.makeText(
                                this@KuisTerjemahanSundaKeLatinActivity,
                                "Tidak ada soal tersedia",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Log.e("KuisActivity", "Response gagal: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<KuisResponse>, t: Throwable) {
                Log.e("KuisActivity", "Error: ${t.message}")
                Toast.makeText(
                    this@KuisTerjemahanSundaKeLatinActivity,
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

        if (kuis.soal.startsWith("http")) {
            soalImage.visibility = View.VISIBLE
            Glide.with(this).load(kuis.soal).into(soalImage)
        } else {
            soalImage.visibility = View.GONE
        }

        btnA.text = kuis.A
        btnB.text = kuis.B
        btnC.text = kuis.C
        btnD.text = kuis.D

        isAnswered = false

        btnA.setOnClickListener { cekJawaban(kuis.A, kuis.jawaban, btnA) }
        btnB.setOnClickListener { cekJawaban(kuis.B, kuis.jawaban, btnB) }
        btnC.setOnClickListener { cekJawaban(kuis.C, kuis.jawaban, btnC) }
        btnD.setOnClickListener { cekJawaban(kuis.D, kuis.jawaban, btnD) }
    }

    private fun cekJawaban(jawabanUser: String, jawabanBenar: String, button: Button) {
        if (isAnswered) return
        isAnswered = true

        val mediaPlayer: MediaPlayer
        val imageRes: Int
        val colorRes: Int

        if (jawabanUser == jawabanBenar) {
            skorBenar++
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_benar) // Suara benar
            imageRes = R.drawable.asset_notif_benar // Gambar jawaban benar
            colorRes = R.color.green
        } else {
            skorSalah++
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

        val skorTotal = skorBenar * 5
        val intent = Intent(this, SkorActivity::class.java)
        intent.putExtra("JUMLAH_BENAR", skorBenar)
        intent.putExtra("JUMLAH_SALAH", skorSalah)
        intent.putExtra("SKOR_TOTAL", skorTotal)
        startActivity(intent)
        finish()

        submitNilaiToAPI(skorTotal) // Simpan skor ke database
    }


    // Simpan Nilai Ke Database untuk Kuis Sunda ke Latin
    private fun submitNilaiToAPI(nilai: Int) {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val nis = sharedPref.getString("NIS", null) // Ambil NIS dari SharedPreferences

        // Jika NIS tidak ditemukan, hentikan proses dan tampilkan error di Logcat
        if (nis.isNullOrEmpty()) {
            Log.e("SESSION_ERROR", "NIS tidak ditemukan dalam sesi")
            return
        }

        val tanggal = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date()) // Format tanggal hari ini
        val nilaiRequest = NilaiRequest(nis, nilai, tanggal) // Buat request data untuk API

        // Panggil API untuk menyimpan nilai Kuis Sunda ke Latin
        ApiClient.instance.simpanNilai(nilaiRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    // Ambil respons dari API dalam bentuk string
                    val responseBody = response.body()?.string() ?: "Response kosong"
                    Log.d("API_RESPONSE", "Raw Response: $responseBody")

                    if (response.isSuccessful) {
                        // Jika penyimpanan berhasil, tampilkan log sukses
                        Log.d("API_SUCCESS", "Nilai berhasil disimpan!")
                        Toast.makeText(this@KuisTerjemahanSundaKeLatinActivity, "Nilai berhasil disimpan!", Toast.LENGTH_SHORT).show()

                        // Pindah ke SkorActivity sambil membawa data jumlah benar, salah, dan skor total
                        val intent = Intent(
                            this@KuisTerjemahanSundaKeLatinActivity,
                            SkorActivity::class.java
                        )
                        intent.putExtra("JUMLAH_BENAR", skorBenar)
                        intent.putExtra("JUMLAH_SALAH", skorSalah)
                        intent.putExtra("SKOR_TOTAL", nilai)
                        startActivity(intent)
                        finish() // Tutup activity saat ini
                    } else {
                        // Jika gagal menyimpan nilai, ambil pesan error dari server
                        val errorBody = response.errorBody()?.string()
                        Log.e("API_ERROR", "Gagal menyimpan nilai: $errorBody")

                        if (errorBody.isNullOrEmpty()) {
                            Log.e(
                                "API_ERROR",
                                "Kemungkinan response dari API tidak valid atau bukan JSON"
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("API_EXCEPTION", "Error parsing response: ${e.message}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Jika API gagal dipanggil, tampilkan error di Logcat
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
}
