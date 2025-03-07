package com.example.widyaaksara.model

data class AksaraModel(
    val nama: String,
    val titik: List<Titik>
)

data class Titik(
    val x: Int,
    val y: Int
)
