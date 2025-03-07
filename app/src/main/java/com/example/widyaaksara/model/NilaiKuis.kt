package com.example.widyaaksara.model

import com.google.gson.annotations.SerializedName

data class NilaiKuis(
    @SerializedName("id") val id: Int,
    @SerializedName("siswa_id") val siswaId: Int,
    @SerializedName("nilai") val nilai: Int,
    @SerializedName("tanggal") val tanggal: String, // Tidak nullable untuk memastikan ada data
    @SerializedName("jam") val jam: String?, // Tambahkan jika ada di API
    @SerializedName("siswa") val siswa: Siswa?
)

data class Siswa(
    @SerializedName("NIS") val NIS: String,
    @SerializedName("NAMA") val NAMA: String
)
