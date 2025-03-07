package com.example.widyaaksara.model

data class AksaraResponse(
    val success: Boolean,
    val data: List<Aksara>
)

data class Aksara(
    val id: Int,
    val nama: String,
    val gambar_aksara: String,
    val gambar_pola: String
)
