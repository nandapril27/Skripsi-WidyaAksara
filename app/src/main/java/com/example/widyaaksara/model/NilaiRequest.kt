package com.example.widyaaksara.model

import com.google.gson.annotations.SerializedName

data class NilaiRequest(
    @SerializedName("NIS") val NIS: String,
    @SerializedName("nilai") val nilai: Int,
    @SerializedName("tanggal") val tanggal: String,

)
