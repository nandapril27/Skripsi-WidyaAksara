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

    //Menampilkan Aksara Swara di Fitur Menulis
    @GET("aksara-swara")
    fun getAksaraSwara(): Call<AksaraResponse>

    //Menampilkan Aksara Ngalagena di Fitur Menulis
    @GET("aksara-ngalagena")
    fun getAksaraNgalagena(): Call<AksaraResponse>

    //Kuis Terjemahan Aksara Ke Latin
    @GET("quiz")
    fun getKuis(): Call<KuisResponse>

    //Kuis Terjemahan Latin Ke Aksara
    @GET("kuis")
    fun getKuisLatinKeAksara(): Call<KuisResponse>

    //Kuis Menulis Aksara Swara
    @GET("menulis-aksara-swara")
    fun getMenulisAksaraSwara(): Call<AksaraResponse>

    //Kuis Menulis Aksara Ngalagena
    @GET("menulis-aksara-ngalagena")
    fun getMenulisAksaraNgalagena(): Call<AksaraResponse>

    //Nilai Kuis Terjemahan Aksara Ke Latin
    @POST("nilai-kuis")
    fun simpanNilai(@Body nilaiRequest: NilaiRequest): Call<ResponseBody>
    @Headers("Cache-Control: no-cache")
    @GET("nilai-kuis/{NIS}")
    fun getNilaiKuis(@Path("NIS") NIS: String): Call<NilaiResponse>

    //Nilai Kuis Terjemahan Latin Ke Aksara
    @POST("nilai-latin-ke-aksara")
    fun simpanNilaiLatin(@Body nilaiRequest: NilaiRequest): Call<ResponseBody>
    @GET("nilai-latin-ke-aksara/{NIS}")
    fun getNilaiKuisLatinKeAksara(@Path("NIS") NIS: String): Call<NilaiResponse>

    //Nilai Kuis Menulis Aksara Swara
    @POST("nilai-menulis-swara")
    fun simpanNilaiMenulisSwara(@Body nilaiRequest: NilaiRequest): Call<ResponseBody>
    @GET("nilai-menulis-swara/{NIS}")
    fun getNilaiKuisMenulisSwara(@Path("NIS") NIS: String): Call<NilaiResponse>

    //Nilai Kuis Menulis Aksara Ngalagena
    @POST("nilai-menulis-ngalagena")
    fun simpanNilaiMenulisNgalagena(@Body nilaiRequest: NilaiRequest): Call<ResponseBody>
    @GET("nilai-menulis-ngalagena/{NIS}")
    fun getNilaiKuisMenulisNgalagena(@Path("NIS") NIS: String): Call<NilaiResponse>

}
