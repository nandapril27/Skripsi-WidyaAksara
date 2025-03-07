package com.example.widyaaksara.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R
import com.example.widyaaksara.api.ApiClient
import com.example.widyaaksara.model.LoginRequest
import com.example.widyaaksara.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Menghubungkan komponen layout dengan kode
        val etNIS = findViewById<EditText>(R.id.etNIS)
        val etNama = findViewById<EditText>(R.id.etNama)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Event klik tombol login
        btnLogin.setOnClickListener {
            val inputNIS = etNIS.text.toString().trim()
            val inputNama = etNama.text.toString().trim()

            // Validasi input
            if (inputNIS.isEmpty() || inputNama.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!inputNIS.matches("\\d+".toRegex())) {
                Toast.makeText(this, "NIS harus berupa angka!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Membuat request login
            val loginRequest = LoginRequest(NIS = inputNIS, NAMA = inputNama)

            // Memanggil API login
            ApiClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (response.isSuccessful && loginResponse != null) {
                        if (loginResponse.success) {
                            val siswaId = loginResponse.data?.id ?: -1 // Default -1 jika null
                            val NIS = loginResponse.data?.NIS ?: ""
                            val NAMA = loginResponse.data?.NAMA ?: ""

                            if (siswaId == -1) {
                                Toast.makeText(this@LoginActivity, "ID siswa tidak valid", Toast.LENGTH_SHORT).show()
                                return
                            }

                            // Simpan data login ke SharedPreferences
                            val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()

                            editor.putInt("siswa_id", siswaId)
                            editor.putString("NIS", NIS)
                            editor.putString("NAMA", NAMA)
                            editor.apply()

                            // Debugging log
                            Log.d("LoginActivity", "Login berhasil: siswa_id=$siswaId, NIS=$NIS, Nama=$NAMA")

                            // Tampilkan pesan berhasil
                            Toast.makeText(this@LoginActivity, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                            // Pindah ke HomeActivity
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.putExtra("NIS", NIS)
                            intent.putExtra("NAMA", NAMA)
                            startActivity(intent)
                            finish()
                        } else {
                            val errorMessage = loginResponse.message ?: "Login gagal"
                            Toast.makeText(this@LoginActivity, "Login Gagal: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Gagal: Respons tidak valid", Toast.LENGTH_SHORT).show()
                        Log.e("LoginActivity", "Error Response: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Gagal terhubung ke server: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "API Error", t)
                }
            })
        }
    }
}
