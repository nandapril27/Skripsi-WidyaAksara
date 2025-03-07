package com.example.widyaaksara.model

data class NilaiSimpan(
    val success: Boolean,
    val message: String,
    val data: NilaiData?
)

data class NilaiData(
    val NIS: String,
    val kuis_id: Int,
    val nilai: Int
)
