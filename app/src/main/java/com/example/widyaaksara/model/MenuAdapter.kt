package com.example.widyaaksara.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.widyaaksara.R

class MenuAdapter(
    private val menuList: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.card_menu)
        val menuTitle: TextView = view.findViewById(R.id.menu_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuName = menuList[position]
        holder.menuTitle.text = menuName

        holder.cardView.setOnClickListener {
            onItemClick(menuName)
        }
    }

    override fun getItemCount(): Int = menuList.size
}
