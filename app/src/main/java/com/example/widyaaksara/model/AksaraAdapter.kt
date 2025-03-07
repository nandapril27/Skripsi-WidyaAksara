package com.example.widyaaksara.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R

class AksaraAdapter(
    private val aksaraList: List<AksaraItem>
) : RecyclerView.Adapter<AksaraAdapter.AksaraViewHolder>() {

    class AksaraViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.aksaraImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AksaraViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aksara, parent, false)
        return AksaraViewHolder(view)
    }

    override fun onBindViewHolder(holder: AksaraViewHolder, position: Int) {
        val item = aksaraList[position]
        holder.imageView.setImageResource(item.aksaraImage)
    }

    override fun getItemCount() = aksaraList.size
}
