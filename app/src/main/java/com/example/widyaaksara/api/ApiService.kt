package com.example.widyaaksara.api

import com.example.widyaaksara.model.AksaraResponse
import com.example.widyaaksara.model.KuisResponse
import com.example.widyaaksara.model.LoginRequest
import com.example.widyaaksara.model.LoginResponse
import com.example.widyaaksara.model.NilaiRequest
import com.example.widyaaksara.model.NilaiResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    //Login
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    //Kuis Aksara Ke Latin
    @GET("quiz")
    fun getKuis(): Call<KuisResponse>

    //Kuis Latin Ke Aksara
    @GET("kuis")
    fun getKuisLatinKeAksara(): Call<KuisResponse>

    //Nilai Kuis Aksara Ke Latin
    @POST("nilai-kuis")
    fun simpanNilai(@Body nilaiRequest: NilaiRequest): Call<ResponseBody>
    @Headers("Cache-Control: no-cache")
    @GET("nilai-kuis/{NIS}")
    fun getNilaiKuis(@Path("NIS") NIS: String): Call<NilaiResponse>

    //Nilai Kuis Latin Ke Aksara
    @POST("nilai-latin-ke-aksara")
    fun simpanNilaiLatin(@Body nilaiRequest: NilaiRequest): Call<ResponseBody>
    @GET("nilai-latin-ke-aksara/{NIS}")
    fun getNilaiKuisLatinKeAksara(@Path("NIS") NIS: String): Call<NilaiResponse>

    //Menampilkan Aksara Swara
    @GET("aksara-swara")
    fun getAksaraSwara(): Call<AksaraResponse>

    //Menampilkan Aksara Ngalagena
    @GET("aksara-ngalagena")
    fun getAksaraNgalagena(): Call<AksaraResponse>

}
