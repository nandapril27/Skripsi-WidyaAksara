package com.example.widyaaksara.model

data class LoginResponse (
    val success: Boolean,
    val message: String,
    val data: siswaData?
)

data class siswaData(
    val id: Int,
    val NIS: String,
    val NAMA: String
)