package com.example.widyaaksara.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.widyaaksara.R

class PetunjukDialog(context: Context, private val namaAksara: String, private val gambar_pola: String) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.petunjuk_penulisan)

        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvInstructions: TextView = findViewById(R.id.tvInstructions)
        val imagePola: ImageView = findViewById(R.id.imagePola) // Sesuai dengan penamaan di activity utama
        val btnClose: Button = findViewById(R.id.btnClose)

        // Set teks judul sesuai aksara yang sedang ditampilkan
        tvTitle.text = "Panduan Penulisan $namaAksara"

        // Set teks petunjuk
        tvInstructions.text = "1. Perhatikeun huruf nu aya dina layar.\n\n" +
                "2. Perhatikeun kana urutan nomer coretan jeung tanda panah dina jero huruf.\n\n" +
                "3. Pék hidep tarik garis dina kanvas bodas di layar, nuturkeun arah panah anu ditangtukeun dumasar kana tahap corétan."

        // Load gambar pola menggunakan Glide
        Glide.with(context)
            .load(gambar_pola)
            .into(imagePola)

        // Tutup dialog saat tombol diklik
        btnClose.setOnClickListener {
            dismiss()
        }
    }
}
