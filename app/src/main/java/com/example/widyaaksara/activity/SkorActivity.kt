package com.example.widyaaksara.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R
import com.example.widyaaksara.api.ApiClient
import com.example.widyaaksara.model.NilaiRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SkorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skor)

        val jumlahBenar = intent.getIntExtra("JUMLAH_BENAR", 0)
        val jumlahSalah = intent.getIntExtra("JUMLAH_SALAH", 0)
        val skorTotal = jumlahBenar * 5  // Kalkulasi skor

        val tvBenar = findViewById<TextView>(R.id.tvBenar)
        val tvSalah = findViewById<TextView>(R.id.tvSalah)
        val tvSkor = findViewById<TextView>(R.id.tvSkor)
        val btnMenu = findViewById<Button>(R.id.btnMenu)

        tvBenar.text = "Benar : $jumlahBenar"
        tvSalah.text = "Salah : $jumlahSalah"
        tvSkor.text = "Nilai   : $skorTotal"

        // Simpan nilai ke SharedPreferences
        simpanNilaiKeSharedPreferences(skorTotal)

        // Cek jenis kuis sebelum menyimpan ke server
        val jenisKuis = intent.getStringExtra("JENIS_KUIS") ?: ""

        if (jenisKuis == "latin_ke_aksara") {
            simpanNilaiKeServer(skorTotal) // API untuk Latin ke Aksara
        } else if (jenisKuis == "aksara_ke_latin") {
            simpanNilaiKeServerSundaKeLatin(skorTotal) // API untuk Aksara ke Latin
        } else {
            println("DEBUG: Tidak ada jenis kuis yang valid!")
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun simpanNilaiKeSharedPreferences(nilai: Int) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("LAST_SCORE", nilai) // Menyimpan skor terakhir
        editor.apply()

        println("DEBUG: Nilai berhasil disimpan ke SharedPreferences -> $nilai")
    }

    //Skor Kuis Latin ke Sunda
    private fun simpanNilaiKeServer(nilai: Int) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val nis = sharedPreferences.getString("NIS", "2021081015") ?: "2021081015"
        val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val nilaiRequest = NilaiRequest(nis, nilai, tanggal)

        println("DEBUG: Mengirim data -> NIS: $nis, Nilai: $nilai")

        ApiClient.instance.simpanNilaiLatin(nilaiRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()?.string()
                println("DEBUG: Response Code -> ${response.code()}")
                println("DEBUG: Response Body -> $responseBody")

                if (response.isSuccessful) {
                    println("DEBUG: Data berhasil disimpan -> $responseBody")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@SkorActivity, "Gagal menyimpan nilai! Response: $errorBody", Toast.LENGTH_LONG).show()
                    println("DEBUG: Response Error -> $errorBody")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SkorActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                println("DEBUG: API Failure -> ${t.message}")
            }
        })
    }

    //Skor Kuis Sunda ke Latin
    private fun simpanNilaiKeServerSundaKeLatin(nilai: Int) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val nis = sharedPreferences.getString("NIS", "2021081015") ?: "2021081015"
        val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val nilaiRequest = NilaiRequest(nis, nilai, tanggal)

        println("DEBUG: Mengirim data (Sunda ke Latin) -> NIS: $nis, Nilai: $nilai")

        ApiClient.instance.simpanNilai(nilaiRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()?.string()
                println("DEBUG: Response Code (Sunda ke Latin) -> ${response.code()}")
                println("DEBUG: Response Body (Sunda ke Latin) -> $responseBody")

                if (response.isSuccessful) {
                    Toast.makeText(this@SkorActivity, "Nilai berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    println("DEBUG: Data berhasil disimpan -> $responseBody")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@SkorActivity, "Gagal menyimpan nilai! Response: $errorBody", Toast.LENGTH_LONG).show()
                    println("DEBUG: Response Error -> $errorBody")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SkorActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                println("DEBUG: API Failure -> ${t.message}")
            }
        })
    }
}
