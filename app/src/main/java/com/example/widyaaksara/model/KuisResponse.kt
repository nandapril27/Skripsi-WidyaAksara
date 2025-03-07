package com.example.widyaaksara.model

import com.google.gson.annotations.SerializedName

data class KuisResponse(
    val soal: String,
    val opsiA: String,
    val opsiB: String,
    val opsiC: String,
    val opsiD: String,
    val jawaban: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<KuisModel>
)
