package com.example.widyaaksara.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.widyaaksara.model.Titik

class CanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }
    private val referencePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 8f
        style = Paint.Style.FILL
    }
    private val path = Path()
    private val paths = mutableListOf<Path>()  // Simpan history path yang digambar pengguna
    private val userPoints = mutableListOf<Titik>()
    private var referencePoints: List<Titik> = listOf()
    private var backgroundBitmap: Bitmap? = null

    private var patternBitmap: Bitmap? = null  // Bitmap untuk gambar pola
    private val patternPaint = Paint().apply { alpha = 100 } // Transparan agar pola tetap terlihat

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    fun setReferencePoints(points: List<Titik>) {
        referencePoints = points
        invalidate()
    }

    // Fungsi untuk menyetel pola aksara dari JSON
    fun setPola(titikList: List<Titik>) {
        referencePoints = titikList  // Simpan titik pola ke referencePoints
        invalidate()  // Perbarui tampilan
    }

    //fungsi agar pola menjadi background
    fun setBackgroundBitmap(bitmap: Bitmap) {
        backgroundBitmap = bitmap
        invalidate() // Paksa redraw agar background ditampilkan
    }


    fun getUserDrawnPoints(): List<Titik> = userPoints

    fun clearCanvas() {
        paths.clear()  // Hapus semua path yang telah digambar
        path.reset()  // Reset path yang sedang digambar
        userPoints.clear()  // Hapus titik-titik yang tersimpan
        invalidate()  // Perbarui tampilan agar terlihat bersih
    }

    fun setPatternImage(bitmap: Bitmap) {
        post { // Tunggu sampai CanvasView sudah memiliki ukuran yang benar
            patternBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
            invalidate() //Refresh tampilan
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("CanvasView", "Mulai menggambar di: ${event.x}, ${event.y}")
                path.reset()  // Reset path lama agar tidak numpuk
                path.moveTo(event.x, event.y)
                userPoints.clear()
                userPoints.add(Titik(event.x.toInt(), event.y.toInt())) // Tambahkan titik pertama
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("CanvasView", "Menggambar di: ${event.x}, ${event.y}")
                path.lineTo(event.x, event.y)
                userPoints.add(Titik(event.x.toInt(), event.y.toInt()))
            }
            MotionEvent.ACTION_UP -> {
                Log.d("CanvasView", "Selesai menggambar")
                paths.add(Path(path)) // Simpan path agar tidak hilang
                path.reset()  // Reset path sementara
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Log.d("CanvasView", "Width: $width, Height: $height") // Cek ukuran canvas

        if (width == 0 || height == 0) {
            Log.e("CanvasView", "CanvasView belum siap!")
            return
        }

        // Gambar background jika ada
        backgroundBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        // Gambar coretan pengguna di atas background
        for (path in paths) {
            canvas.drawPath(path, paint)
        }

        // Gambar path terbaru yang sedang digambar user
        canvas.drawPath(path, paint)

        // Gambar titik referensi (dalam warna merah)
        for (titik in referencePoints) {
            canvas.drawCircle(titik.x.toFloat(), titik.y.toFloat(), 10f, referencePaint)
        }
    }

}
