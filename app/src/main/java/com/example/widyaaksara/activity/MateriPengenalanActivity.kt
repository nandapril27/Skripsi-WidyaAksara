package com.example.widyaaksara.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.widyaaksara.R
import java.io.BufferedReader
import java.io.InputStreamReader
//import com.github.barteksc.pdfviewer.PDFView

class MateriPengenalanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_pengenalan)

        val textViewMateri = findViewById<TextView>(R.id.textViewMateri)
        textViewMateri.text = readTextFile()
    }

    private fun readTextFile(): String {
        val inputStream = resources.openRawResource(R.raw.materi)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }

    }
}

// baca pdf
//        val pdfView = findViewById<PDFView>(R.id.pdfView)
//
//        // Load PDF dari folder raw
//        pdfView.fromAsset("materi.pdf")
//            .enableSwipe(true)
//            .swipeHorizontal(false)
//            .enableDoubletap(true)
//            .load()

