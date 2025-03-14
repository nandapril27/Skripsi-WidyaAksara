package com.example.widyaaksara.utils

import android.content.Context
import android.util.Log
import com.example.widyaaksara.model.AksaraModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type

object JsonHelper {
    // Fungsi umum untuk membaca JSON dari assets
    private fun loadJsonFromAssets(context: Context, filename: String): List<AksaraModel> {
        return try {
            val inputStream = context.assets.open(filename)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            Log.d("JsonHelper", "JSON berhasil dibaca: $jsonString")

            val listType: Type = object : TypeToken<List<AksaraModel>>() {}.type
            val result = Gson().fromJson<List<AksaraModel>>(jsonString, listType)

            if (result.isNullOrEmpty()) {
                Log.e("JsonHelper", "Parsing JSON gagal atau data kosong!")
            } else {
                Log.d("JsonHelper", "Jumlah aksara dalam JSON: ${result.size}")
            }

            result ?: emptyList() // Jika parsing gagal, kembalikan list kosong
        } catch (e: IOException) {
            Log.e("JsonHelper", "Error membaca JSON: ${e.message}")
            e.printStackTrace()
            emptyList()
        } catch (e: JsonSyntaxException) {
            Log.e("JsonHelper", "Error parsing JSON: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    // Fungsi khusus untuk membaca aksara swara
    fun loadAksaraSwara(context: Context): List<AksaraModel> {
        return loadJsonFromAssets(context, "pola_aksara_swara.json")
    }

    // Fungsi khusus untuk membaca aksara ngalagena
    fun loadAksaraNgalagena(context: Context): List<AksaraModel> {
        return loadJsonFromAssets(context, "pola_aksara_ngalagena.json")
    }
}
