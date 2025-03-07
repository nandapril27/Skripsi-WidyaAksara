package com.example.widyaaksara.model

data class NilaiResponse(
    val success: Boolean,
    val message: String,
    val data: List<NilaiKuis> // Ambil data sebagai list
)
