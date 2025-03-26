package com.example.widyaaksara.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R

class BantuanAdapter(private val listBantuan: List<Bantuan>) :
    RecyclerView.Adapter<BantuanAdapter.BantuanViewHolder>() {

    class BantuanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BantuanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bantuan, parent, false)
        return BantuanViewHolder(view)
    }

    override fun onBindViewHolder(holder: BantuanViewHolder, position: Int) {
        val bantuan = listBantuan[position]
        holder.imgIcon.setImageResource(bantuan.icon)
        holder.tvTitle.text = bantuan.title
        holder.tvDescription.text = bantuan.description
    }

    override fun getItemCount(): Int = listBantuan.size
}
