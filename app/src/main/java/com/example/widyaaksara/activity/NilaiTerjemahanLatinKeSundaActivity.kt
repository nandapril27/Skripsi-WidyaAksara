package com.example.widyaaksara.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R
import com.example.widyaaksara.api.ApiClient
import com.example.widyaaksara.model.NilaiAdapter
import com.example.widyaaksara.model.NilaiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NilaiTerjemahanLatinKeSundaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NilaiAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nilai_terjemahan_latin_ke_sunda)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi adapter dengan list kosong agar tidak null
        adapter = NilaiAdapter(emptyList())
        recyclerView.adapter = adapter

        // Ambil NIS siswa dari SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val NIS = sharedPreferences.getString("NIS", null)

        Log.d("NilaiActivity", "NIS yang diambil: $NIS")

        if (NIS.isNullOrEmpty()) {
            Toast.makeText(this, "Gagal mendapatkan NIS siswa. Silakan login ulang.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Log.d("NilaiActivity", "NIS yang dikirim ke API: $NIS")
            fetchNilai(NIS) // Kirim NIS sebagai String
        }
    }

    override fun onResume() {
        super.onResume()
        val nis = sharedPreferences.getString("NIS", null)
        if (!nis.isNullOrEmpty()) {
            fetchNilai(nis) // Memastikan histori diperbarui saat kembali ke halaman
        }
    }

    private fun fetchNilai(nis: String) {
        ApiClient.instance.getNilaiKuisLatinKeAksara(nis).enqueue(object : Callback<NilaiResponse> {
            override fun onResponse(call: Call<NilaiResponse>, response: Response<NilaiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val nilaiList = response.body()?.data ?: emptyList()
                    Log.d("API Response", "Data Nilai: ${response.body()}")

                    if (::adapter.isInitialized) {
                        if (adapter.itemCount != nilaiList.size) { // Hanya perbarui jika jumlah item berubah
                            adapter.updateData(nilaiList)
                            adapter.notifyDataSetChanged() // Tambahkan ini untuk memastikan RecyclerView diperbarui
                        }
                    } else {
                        adapter = NilaiAdapter(nilaiList)
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("API Error", "Gagal mengambil data: ${response.errorBody()?.string()}")
                    Toast.makeText(this@NilaiTerjemahanLatinKeSundaActivity, "Gagal mengambil data nilai", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NilaiResponse>, t: Throwable) {
                Log.e("API Failure", "Terjadi kesalahan: ${t.message}")
                Toast.makeText(this@NilaiTerjemahanLatinKeSundaActivity, "Terjadi kesalahan jaringan", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
