package com.example.widyaaksara.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R

class NilaiAdapter(private var nilaiList: List<NilaiKuis>) : RecyclerView.Adapter<NilaiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNilai: TextView = view.findViewById(R.id.tvNilai)
        val tvNIS: TextView = view.findViewById(R.id.tvNIS)
        val tvNAMA: TextView = view.findViewById(R.id.tvNAMA)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvJam: TextView = view.findViewById(R.id.tvJam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nilai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nilai = nilaiList[position]
        Log.d("NilaiAdapter", "Data yang diterima: $nilai")

        holder.tvNIS.text = "NIS: ${nilai.siswa?.NIS ?: "-"}"
        holder.tvNAMA.text = "Nama: ${nilai.siswa?.NAMA ?: "-"}"
        holder.tvNilai.text = "Nilai: ${nilai.nilai}"
        holder.tvTanggal.text = "Tanggal: ${nilai.tanggal}"
        holder.tvJam.text = "Jam: ${nilai.jam ?: "-"}" // Jika jam null, tampilkan "-"
    }

    override fun getItemCount(): Int {
        return nilaiList.size
    }

    // Fungsi untuk memperbarui data dalam adapter
    fun updateData(newList: List<NilaiKuis>) {
        println("DEBUG: Data sebelum diperbarui: $nilaiList")
        println("DEBUG: Data baru yang diterima: $newList")

        nilaiList = newList
        notifyDataSetChanged() // Atau notifyItemRangeChanged(0, newList.size)

        println("DEBUG: Data setelah diperbarui: $nilaiList")
    }

}
