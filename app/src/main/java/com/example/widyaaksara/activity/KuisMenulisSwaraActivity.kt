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
            if (currentAksara == null) {
                Toast.makeText(this, "Tunggu hingga data selesai dimuat!", Toast.LENGTH_SHORT).show()
            } else {
                validateDrawing()
            }
        }

        // Menangani event sentuhan di canvasView
        canvasView.setOnTouchListener { _, event ->
            canvasView.onTouchEvent(event)
            true
        }

//        // Pastikan gambar pola ditampilkan setelah CanvasView siap
//        canvasView.post {
//            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.gambar_pola)
//            canvasView.setPatternImage(bitmap)
//        }

        // Membaca data JSON dari assets
        aksaraSwaraList = JsonHelper.loadAksaraSwara(this)
        if (aksaraSwaraList.isNotEmpty()) {
            Log.d("AksaraSwara", "Berhasil membaca JSON, jumlah aksara: ${aksaraSwaraList.size}")
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
        if (aksaraList.isNotEmpty() && currentIndex < aksaraList.size) {
            val aksara = aksaraList[currentIndex]
            tvNamaAksara.text = aksara.nama

            Glide.with(this)
                .load(aksara.gambar_aksara)
                .into(imageAksara)

            Glide.with(this)
                .load(aksara.gambar_pola)
                .into(imagePola)

            // Set current aksara untuk validasi gambar
            currentAksara = aksaraSwaraList.find { it.nama == aksara.nama }

            if (currentAksara != null) {
                tampilkanPolaAksara()
                Log.d("updateUI", "currentAksara diisi: ${currentAksara?.nama}")
            } else {
                Log.e("updateUI", "currentAksara masih null setelah updateUI")
            }

            // Menampilkan pola di CanvasView
            tampilkanPolaAksara()
        }
    }
//        // Ubah gambar pola menjadi bitmap dan jadikan background di CanvasView
//        loadBitmapFromUrl(aksara.gambar_pola) { bitmap ->
//            if (bitmap != null) {
//                canvasView.setBackgroundBitmap(bitmap)
//            }
//        }

    /**
    //     * Validasi gambar pengguna dengan pola referensi menggunakan Point in Polygon (PIP)
    //     */
    private fun validateDrawing() {
        if (currentAksara == null) {
            Log.e("validateDrawing", "currentAksara masih null!")
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
        val referencePoints: List<Titik> = currentAksara?.titik ?: emptyList()
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
    fun isPointInPolygon(userPoints: List<PointF>, referencePoints: List<PointF>): Boolean {
        for (point in userPoints) { // Periksa setiap titik user
            var intersections = 0
            val size = referencePoints.size

            for (i in 0 until size) {
                val v1 = referencePoints[i]
                val v2 = referencePoints[(i + 1) % size] // Loop kembali ke titik pertama

                // Cek apakah garis horizontal dari titik memotong sisi poligon
                if ((v1.y > point.y) != (v2.y > point.y)) {
                    val intersectX = v1.x + (point.y - v1.y) * (v2.x - v1.x) / (v2.y - v1.y)
                    if (point.x < intersectX) {
                        intersections++
                    }
                }
            }

            // Jika titik berada di luar poligon, langsung return false
            if (intersections % 2 == 0) {
                return false
            }
        }

        return true // Semua titik user berada dalam poligon
    }

}




//        //Ambil gambar pola dari URL dan set sebagai background di CanvasView
//            Glide.with(this)
//                .asBitmap()
//                .load(aksara.gambar_pola)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
//                    ) {
//                        Log.d(
//                            "CanvasView",
//                            "Bitmap berhasil dimuat: ${resource.width}x${resource.height}"
//                        ) // Debug
//                        canvasView.setPatternImage(resource) // Fungsi ini akan kita buat di step berikutnya
//                    }
//
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        Log.e("CanvasView", "Gambar dihapus dari Glide")
//                    }
//
//                    override fun onLoadFailed(errorDrawable: Drawable?) {
//                        Log.e(
//                            "CanvasView",
//                            "Gagal memuat gambar pola dari URL: ${aksara.gambar_pola}"
//                        )
//                    }
//                })





//class KuisMenulisSwaraActivity : AppCompatActivity() {
//    private lateinit var canvasView: CanvasView
//    private var aksaraList: List<AksaraModel>? = null
//    private var currentAksara: AksaraModel? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_kuis_menulis_swara)
//
//        // Inisialisasi UI
//        val imageAksara: ImageView = findViewById(R.id.imageAksara)
//        val tvNamaAksara: TextView = findViewById(R.id.tvNamaAksara)
//        canvasView = findViewById(R.id.canvasView)
//
//
//        // Membaca JSON dari assets
//        aksaraList = JsonHelper.parseAksaraJson(this, "pola_aksara_swara.json")
//
//        // Menampilkan aksara pertama
//        aksaraList?.firstOrNull()?.let { aksara ->
//            currentAksara = aksara
//            tvNamaAksara.text = aksara.nama
//
//            // Load gambar aksara dari drawable
//            val resourceId = resources.getIdentifier(aksara.nama, "drawable", packageName)
//            if (resourceId != 0) {
//                imageAksara.setImageResource(resourceId)
//            } else {
//                Log.e("KuisMenulisSwara", "Gambar tidak ditemukan untuk ${aksara.nama}")
//            }
//
//            // Mengirim pola titik ke CanvasView untuk ditampilkan
//            canvasView.setReferencePoints(aksara.titik)
//        } ?: Log.e("KuisMenulisSwara", "Gagal membaca JSON!")
//    }
//
//    /**
//     * Validasi gambar pengguna dengan pola referensi menggunakan Point in Polygon (PIP)
//     */
//    private fun validateDrawing() {
//        val userPoints = canvasView.getUserDrawnPoints()
//        val referencePoints = currentAksara?.titik ?: return
//
//        val isCorrect = isPointInPolygon(userPoints, referencePoints)
//        if (isCorrect) {
//            Log.d("KuisMenulisSwara", "Gambar benar!")
//        } else {
//            Log.d("KuisMenulisSwara", "Gambar belum sesuai.")
//        }
//    }
//
//    /**
//     * Algoritma Point in Polygon untuk membandingkan hasil gambar dengan pola referensi.
//     */
//    private fun isPointInPolygon(userPoints: List<Titik>, refPoints: List<Titik>): Boolean {
//        if (userPoints.isEmpty() || refPoints.isEmpty()) return false
//
//        val threshold = 20  // Jarak maksimum perbedaan titik
//
//        for (userPoint in userPoints) {
//            val isMatching = refPoints.any { refPoint ->
//                val dx = userPoint.x - refPoint.x
//                val dy = userPoint.y - refPoint.y
//                (dx * dx + dy * dy) <= (threshold * threshold)  // Pythagoras untuk jarak
//            }
//            if (!isMatching) return false
//        }
//        return true
//    }
//}
