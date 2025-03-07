package com.example.widyaaksara.model

import com.google.gson.annotations.SerializedName

data class KuisModel(
    @SerializedName("id") val id: Int,
    @SerializedName("soal") val soal: String,
    @SerializedName("A") val A: String,
    @SerializedName("B") val B: String,
    @SerializedName("C") val C: String,
    @SerializedName("D") val D: String,
    @SerializedName("jawaban") val jawaban: String
)
