package com.example.widyaaksara.utils

import android.content.Context
import com.example.widyaaksara.model.AksaraModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object JsonHelper {
    // Fungsi umum untuk membaca JSON dari assets
    private fun loadJsonFromAssets(context: Context, filename: String): List<AksaraModel> {
        return try {
            val inputStream = context.assets.open(filename)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<AksaraModel>>() {}.type
            Gson().fromJson(jsonString, listType)
        } catch (e: IOException) {
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


//object JsonHelper {
//    fun loadJsonFromAssets(context: Context, filename: String): String? {
//        return try {
//            context.assets.open(filename).bufferedReader().use { it.readText() }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    fun parseAksaraJson(context: Context, fileName: String): List<AksaraModel>? {
//        return try {
//            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
//            val listType = object : TypeToken<List<AksaraModel>>() {}.type
//            Gson().fromJson(jsonString, listType)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//}
