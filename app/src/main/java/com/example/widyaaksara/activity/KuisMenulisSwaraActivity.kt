package com.example.widyaaksara.activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.example.widyaaksara.R
import com.example.widyaaksara.api.ApiClient
import com.example.widyaaksara.model.Aksara
import com.example.widyaaksara.model.AksaraModel
import com.example.widyaaksara.model.AksaraResponse
import com.example.widyaaksara.model.Titik
import com.example.widyaaksara.utils.JsonHelper
import com.example.widyaaksara.view.CanvasView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.pow
import kotlin.math.sqrt

class KuisMenulisSwaraActivity : AppCompatActivity() {
    private lateinit var btnNext: ImageButton
    private lateinit var btnBack: ImageButton
    private lateinit var imagePola: ImageView
    private lateinit var canvasView: CanvasView
    private lateinit var tvNamaAksara: TextView
    private lateinit var imageAksara: ImageView
    private lateinit var btnValidasi: Button

    private var aksaraList: List<Aksara> = listOf()
    private var aksaraSwaraList: List<AksaraModel> = listOf()
    private var currentIndex = 0
    private var currentAksara: AksaraModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuis_menulis_swara)

        btnNext = findViewById(R.id.btnNext)
        btnBack = findViewById(R.id.btnBack)
        imagePola = findViewById(R.id.imagePola)
        canvasView = findViewById(R.id.canvasView)
        tvNamaAksara = findViewById(R.id.tvNamaAksara)
        imageAksara = findViewById(R.id.imageAksara)
        btnValidasi = findViewById(R.id.btnValidasi)

        btnValidasi.setOnClickListener {
            val userPoints = canvasView.getUserDrawnPoints()
            if (userPoints.isNotEmpty()) {
                validateDrawing() // Panggil validasi hanya jika ada gambar
                Log.d("CanvasView", "Total titik yang digambar: ${userPoints.size}")
            } else {
                Toast.makeText(this, "Silakan gambar dulu!", Toast.LENGTH_SHORT).show()
            }
        }

        // Menangani event sentuhan di canvasView
        canvasView.setOnTouchListener { _, event ->
            canvasView.onTouchEvent(event)
            true
        }


        // Membaca data JSON dari assets
        aksaraSwaraList = JsonHelper.loadAksaraSwara(this)
        if (aksaraSwaraList.isNotEmpty()) {
            Log.d("AksaraSwara", "Berhasil membaca JSON, jumlah aksara: ${aksaraSwaraList.size}")
            Log.d("AksaraSwara", "Data JSON: ${aksaraSwaraList.joinToString { it.nama }}")

            // Pastikan updateUI dipanggil setelah JSON berhasil dimuat
            updateUI()

        } else {
            Log.e("AksaraSwara", "Gagal membaca JSON!")
        }

        // Memanggil API untuk mendapatkan data aksara
        fetchAksaraData()

        btnNext.setOnClickListener {
            if (aksaraList.isNotEmpty()) {
                canvasView.clearCanvas()  // Bersihkan sebelum menggambar aksara berikutnya
                currentIndex = (currentIndex + 1) % aksaraList.size
                updateUI()
            }
        }

        btnBack.setOnClickListener {
            if (aksaraList.isNotEmpty()) {
                canvasView.clearCanvas()  // Bersihkan sebelum kembali ke aksara sebelumnya
                currentIndex = if (currentIndex - 1 < 0) aksaraList.size - 1 else currentIndex - 1
                updateUI()
            }
        }

    }

    private fun tampilkanPolaAksara() {
        val aksara = aksaraSwaraList.find { it.nama == aksaraList[currentIndex].nama }
        if (aksara != null && ::canvasView.isInitialized) {
            canvasView.setPola(aksara.titik) // Kirim titik-titik pola ke CanvasView
        } else {
            Log.e("CanvasView", "Pola tidak ditemukan untuk aksara: ${aksaraList[currentIndex].nama}")
        }
    }

    private fun fetchAksaraData() {
        ApiClient.instance.getAksaraSwara().enqueue(object : Callback<AksaraResponse> {
            override fun onResponse(
                call: Call<AksaraResponse>,
                response: Response<AksaraResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("fetchAksaraData", "Response berhasil diterima!")

                    val responseData = response.body()
                    Log.d("fetchAksaraData", "Data dari API: $responseData")

                    aksaraList = responseData?.data ?: listOf()
                    if (aksaraList.isNotEmpty()) {
                        Log.d("fetchAksaraData", "Jumlah data diterima: ${aksaraList.size}")
                        currentIndex = 0 // Set indeks ke data pertama
                        updateUI()
                    } else {
                        Log.e("fetchAksaraData", "Data dari API kosong!")
                    }
                } else {
                    Log.e(
                        "fetchAksaraData",
                        "Gagal mengambil data! Response code: ${response.code()}"
                    )
                    Toast.makeText(
                        this@KuisMenulisSwaraActivity,
                        "Gagal mengambil data!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AksaraResponse>, t: Throwable) {
                Log.e("fetchAksaraData", "Kesalahan jaringan: ${t.message}")
                Toast.makeText(
                    this@KuisMenulisSwaraActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateUI() {
        Log.d("updateUI", "Ukuran aksaraList: ${aksaraList.size}, currentIndex: $currentIndex")
        // Reset index jika melebihi batas
        if (currentIndex >= aksaraList.size) {
            Log.w("updateUI", "currentIndex melebihi batas, reset ke 0")
            currentIndex = 0
        }

        if (aksaraList.isNotEmpty() && currentIndex < aksaraList.size) {
            val aksara = aksaraList[currentIndex]
            tvNamaAksara.text = aksara.nama

            Glide.with(this)
                .load(aksara.gambar_aksara)
                .into(imageAksara)

            Glide.with(this)
                .load(aksara.gambar_pola)
                .into(imagePola)

            Log.d("updateUI", "Mencari currentAksara untuk: ${aksara.nama}")

            // Cek apakah aksaraSwaraList berisi data yang benar
            Log.d("updateUI", "aksaraSwaraList: ${aksaraSwaraList.map { it.nama }}")

            // Debug log sebelum mencari currentAksara
            Log.d("updateUI", "Mencari currentAksara untuk: ${aksara.nama}")

            // Loop untuk mencari aksara yang cocok
            for (aksaraSwara in aksaraSwaraList) {
                val namaBersih = aksaraSwara.nama.substringAfterLast("_")
                    .trim() // Ambil bagian terakhir setelah underscore
                val namaDicari = aksara.nama.trim()

                Log.d("updateUI", "Membandingkan: $namaBersih dengan ${aksara.nama}")

                if (namaBersih == namaDicari) {
                    Log.d("updateUI", "Cocok! currentAksara di-set ke: ${aksaraSwara.nama}")
                    currentAksara = aksaraSwara
                    break // Hentikan loop jika ditemukan
                }
            }

            // Debugging log setelah loop selesai
            if (currentAksara != null) {
                Log.d("updateUI", "Setelah loop: currentAksara = ${currentAksara!!.nama}")
            } else {
                Log.e(
                    "updateUI",
                    "Setelah loop: currentAksara masih null! Mungkin tidak ditemukan dalam aksaraSwaraList."
                )
            }

            // Jika currentAksara masih null, cek dengan metode alternatif (hanya jika belum ditemukan)
            if (currentAksara == null) {
                currentAksara = aksaraSwaraList.find {
                    it.nama.replace(Regex("^\\d+\\."), "") == aksara.nama
                }

                if (currentAksara != null) {
                    Log.d(
                        "updateUI",
                        "currentAksara ditemukan dengan .find(): ${currentAksara!!.nama}"
                    )
                } else {
                    Log.e("updateUI", "currentAksara masih null setelah metode .find()")
                }
            }

            // Hanya tampilkan pola jika currentAksara tidak null
            if (currentAksara != null) {
                tampilkanPolaAksara()
            } else {
                Log.e("updateUI", "currentAksara null, tidak bisa menampilkan pola aksara!")
            }
        }
    }

    /**
    //     * Validasi gambar pengguna dengan pola referensi menggunakan Point in Polygon (PIP)
    //     */
    private fun validateDrawing() {
        if (currentAksara == null || currentAksara?.titik.isNullOrEmpty()) {
            Log.e("validateDrawing", "Pola referensi tidak ditemukan atau kosong!")
            Toast.makeText(this, "Pola belum dimuat, coba lagi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil titik yang digambar user
        val userPoints: List<Titik> = canvasView.getUserDrawnPoints()
        Log.d("validateDrawing", "Jumlah titik pengguna: ${userPoints.size}")

        if (userPoints.isEmpty()) {
            Toast.makeText(this, "Silakan gambar dulu sebelum validasi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Konversi titik user ke List<PointF>
        val userPointsF: List<PointF> = convertToPointFList(userPoints)

        // Ambil titik pola referensi
        val referencePoints: List<Titik> = currentAksara!!.titik  // Sudah aman karena dicek di atas
        val referencePointsF: List<PointF> = convertToPointFList(referencePoints)

        // Validasi menggunakan Point in Polygon
        val isCorrect = isPointInPolygon(userPointsF, referencePointsF)

        if (isCorrect) {
            Log.d("KuisMenulisSwara", "Gambar benar!")
            Toast.makeText(this, "Gambar benar!", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("KuisMenulisSwara", "Gambar belum sesuai.")
            Toast.makeText(this, "Gambar belum sesuai, coba lagi!", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi konversi List<Titik> ke List<PointF>
    fun convertToPointFList(titikList: List<Titik>): List<PointF> {
        return titikList.map { PointF(it.x.toFloat(), it.y.toFloat()) }
    }


    /**
     * Algoritma Point in Polygon untuk membandingkan hasil gambar dengan pola referensi.
     */
    fun isPointInPolygon(userPoints: List<PointF>, referencePoints: List<PointF>, tolerance: Float = 10f): Boolean {
        if (userPoints.isEmpty() || referencePoints.isEmpty()) return false

        var correctPoints = 0

        for (userPoint in userPoints) {
            var intersections = 0
            val size = referencePoints.size

            for (i in 0 until size) {
                val v1 = referencePoints[i]
                val v2 = referencePoints[(i + 1) % size]

                // Cek apakah garis horizontal dari titik memotong sisi poligon
                if ((v1.y > userPoint.y) != (v2.y > userPoint.y)) {
                    val intersectX = v1.x + (userPoint.y - v1.y) * (v2.x - v1.x) / (v2.y - v1.y)
                    if (userPoint.x < intersectX) {
                        intersections++
                    }
                }
            }

            // Cek apakah titik berada di dalam poligon (menggunakan aturan ray-casting)
            val insidePolygon = intersections % 2 == 1

            // Tambahkan toleransi dengan membandingkan jarak titik user dengan titik referensi
            val closeEnough = referencePoints.any { refPoint ->
                distance(userPoint, refPoint) <= tolerance
            }

            if (insidePolygon || closeEnough) {
                correctPoints++
            }
        }

        // Pastikan setidaknya 70% titik pengguna cocok dengan referensi
        val accuracy = correctPoints.toFloat() / userPoints.size
        return accuracy >= 0.7
    }

    // Fungsi untuk menghitung jarak antara dua titik
    fun distance(p1: PointF, p2: PointF): Float {
        return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
    }


}
