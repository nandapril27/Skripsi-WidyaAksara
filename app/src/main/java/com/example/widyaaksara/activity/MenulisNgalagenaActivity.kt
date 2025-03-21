package com.example.widyaaksara.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.graphics.PointF
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.widyaaksara.R
import com.example.widyaaksara.api.ApiClient
import com.example.widyaaksara.dialog.PetunjukDialog
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
import kotlin.math.abs

class MenulisNgalagenaActivity : AppCompatActivity() {
    private lateinit var btnNext: ImageButton
    private lateinit var btnBack: ImageButton
    private lateinit var imagePola: ImageView
    private lateinit var canvasView: CanvasView
    private lateinit var tvNamaAksara: TextView
    private lateinit var imageAksara: ImageView
    private lateinit var btnValidasi: Button
    private lateinit var ivPetunjuk: ImageView

    private var aksaraList: List<Aksara> = listOf()
    private var aksaraNgalagenaList: List<AksaraModel> = listOf()
    private var currentIndex = 0
    private var currentAksara: AksaraModel? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menulis_ngalagena)

        btnNext = findViewById(R.id.btnNext)
        btnBack = findViewById(R.id.btnBack)
        imagePola = findViewById(R.id.imagePola)
        canvasView = findViewById(R.id.canvasView)
        tvNamaAksara = findViewById(R.id.tvNamaAksara)
        imageAksara = findViewById(R.id.imageAksara)
        btnValidasi = findViewById(R.id.btnValidasi)
        ivPetunjuk = findViewById(R.id.ivPetunjuk)

        // Event klik untuk tombol Petunjuk penulisan
        ivPetunjuk .setOnClickListener {
            if (aksaraList.isNotEmpty()) {
                val aksara = aksaraList[currentIndex]
                PetunjukDialog(this, aksara.nama, aksara.gambar_pola).show()
            }
        }

        fetchAksaraData()
        btnValidasi.setOnClickListener {
            val userPoints = canvasView.getUserDrawnPoints()
            if (userPoints.isNotEmpty()) {
                validateDrawing() // Panggil validasi hanya jika ada gambar
                Log.d("CanvasView", "Total titik yang digambar: ${userPoints.size}")
            } else {
                val mediaPlayer = MediaPlayer.create(this, R.raw.sound_salah) // Suara peringatan
                mediaPlayer.start() // Mainkan suara

                // Tampilkan dialog dengan gambar peringatan
                tampilkanDialogJawaban(R.drawable.asset_notif_peringatan)

                // Lepaskan media player setelah selesai diputar
                mediaPlayer.setOnCompletionListener { mp -> mp.release() }
            }
        }

        // Menangani event sentuhan di canvasView
        canvasView.setOnTouchListener { _, event ->
            canvasView.onTouchEvent(event)
            true
        }


        // Membaca data JSON dari assets
        aksaraNgalagenaList = JsonHelper.loadAksaraNgalagena(this)
        if (aksaraNgalagenaList.isNotEmpty()) {
            Log.d("AksaraNgalagena", "Berhasil membaca JSON, jumlah aksara: ${aksaraNgalagenaList.size}")
            Log.d("AksaraNgalagena", "Data JSON: ${aksaraNgalagenaList.joinToString { it.nama }}")

            // Pastikan updateUI dipanggil setelah JSON berhasil dimuat
            updateUI()

        } else {
            Log.e("AksaraNgalagena", "Gagal membaca JSON!")
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
        val aksara = aksaraNgalagenaList.find { it.nama == aksaraList[currentIndex].nama }
        if (aksara != null && ::canvasView.isInitialized) {
            canvasView.setPola(aksara.titik) // Kirim titik-titik pola ke CanvasView
        } else {
            Log.e("CanvasView", "Pola tidak ditemukan untuk aksara: ${aksaraList[currentIndex].nama}")
        }
    }

    private fun fetchAksaraData() {
        ApiClient.instance.getAksaraNgalagena().enqueue(object : Callback<AksaraResponse> {
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
                        this@MenulisNgalagenaActivity,
                        "Gagal mengambil data!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AksaraResponse>, t: Throwable) {
                Log.e("fetchAksaraData", "Kesalahan jaringan: ${t.message}")
                Toast.makeText(
                    this@MenulisNgalagenaActivity,
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

            // Cek apakah aksaraNgalagenaList berisi data yang benar
            Log.d("updateUI", "aksaraSwaraList: ${aksaraNgalagenaList.map { it.nama }}")

            // Debug log sebelum mencari currentAksara
            Log.d("updateUI", "Mencari currentAksara untuk: ${aksara.nama}")

            // Loop untuk mencari aksara yang cocok
            for (aksaraNgalagena in aksaraNgalagenaList) {
                val namaBersih = aksaraNgalagena.nama.substringAfterLast("_")
                    .trim() // Ambil bagian terakhir setelah underscore
                val namaDicari = aksara.nama.trim()

                Log.d("updateUI", "Membandingkan: $namaBersih dengan ${aksara.nama}")

                if (namaBersih == namaDicari) {
                    Log.d("updateUI", "Cocok! currentAksara di-set ke: ${aksaraNgalagena.nama}")
                    currentAksara = aksaraNgalagena
                    break // Hentikan loop jika ditemukan
                }
            }

            // Debugging log setelah loop selesai
            if (currentAksara != null) {
                Log.d("updateUI", "Setelah loop: currentAksara = ${currentAksara!!.nama}")
            } else {
                Log.e(
                    "updateUI",
                    "Setelah loop: currentAksara masih null! Mungkin tidak ditemukan dalam aksaraNgalagenaList."
                )
            }

            // Jika currentAksara masih null, cek dengan metode alternatif (hanya jika belum ditemukan)
            if (currentAksara == null) {
                currentAksara = aksaraNgalagenaList.find {
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

        // Gunakan cekJawaban() untuk menampilkan dialog dan suara
        cekJawaban(isCorrect)
    }

    // Fungsi konversi List<Titik> ke List<PointF>
    fun convertToPointFList(titikList: List<Titik>): List<PointF> {
        return titikList.map { PointF(it.x.toFloat(), it.y.toFloat()) }
    }

    private fun cekJawaban(isCorrect: Boolean) {
        var isAnswered = true

        val mediaPlayer: MediaPlayer
        val imageRes: Int

        if (isCorrect) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_benar) // Suara benar
            imageRes = R.drawable.asset_notif_benar_nulis // Gambar jawaban benar
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_salah) // Suara salah
            imageRes = R.drawable.asset_notif_salah_nulis // Gambar jawaban salah
        }
        mediaPlayer.start() // Mainkan suara

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

        // Atur dialog agar otomatis hilang setelah 2 detik
        Handler(Looper.getMainLooper()).postDelayed({
            alertDialog.dismiss()
        }, 1500) // 1500ms = 1.5 detik
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

            Log.d("PIP", "Memeriksa titik pengguna: (${userPoint.x}, ${userPoint.y})")

            for (i in 0 until size) {
                val v1 = referencePoints[i]
                val v2 = referencePoints[(i + 1) % size]

                // Pengecekan apakah titik pengguna berada di vertex (sudut) dari poligon
                if ((userPoint.x == v1.x && userPoint.y == v1.y) || (userPoint.x == v2.x && userPoint.y == v2.y)) {
                    return true // Titik berada di vertex, langsung dianggap valid
                }

                // Pengecekan apakah titik pengguna berada di garis tepi (boundary) poligon
                if (isPointOnLineSegment(userPoint, v1, v2, tolerance)) {
                    return true // Titik berada di boundary, langsung dianggap valid
                }

                // Pengecekan apakah garis horizontal dari titik memotong sisi poligon (Ray-Casting)
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

        // Pastikan setidaknya 80% titik pengguna cocok dengan referensi
        val accuracy = correctPoints.toFloat() / userPoints.size
        return accuracy >= 0.8
    }

    // Fungsi untuk menghitung jarak antara dua titik
    fun distance(p1: PointF, p2: PointF): Float {
        return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
    }

    // Function untuk Pengecekan apakah titik pengguna berada di garis tepi (boundary) poligon
    fun isPointOnLineSegment(p: PointF, a: PointF, b: PointF, tolerance: Float): Boolean {
        val crossProduct = (p.y - a.y) * (b.x - a.x) - (p.x - a.x) * (b.y - a.y)
        if (abs(crossProduct) > tolerance) return false  // Jika tidak sejajar, berarti bukan di garis

        val dotProduct = (p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y)
        if (dotProduct < 0) return false  // Jika titik berada sebelum titik pertama

        val squaredLengthBA = (b.x - a.x).pow(2) + (b.y - a.y).pow(2)
        if (dotProduct > squaredLengthBA) return false  // Jika titik berada setelah titik kedua

        return true  // Jika lolos semua kondisi, titik berada di garis tepi (boundary)
    }

}