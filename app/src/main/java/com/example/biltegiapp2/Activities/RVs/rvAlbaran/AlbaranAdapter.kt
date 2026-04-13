package com.example.biltegiapp2.Activities.RVs.rvAlbaran

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.DB.Tablak.Albaran
import com.example.biltegiapp2.R

class AlbaranAdapter(private val albarans: List<Albaran>, private val onItemSelected: (Albaran) -> Unit): RecyclerView.Adapter<AlbaranViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbaranViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_albaran, parent, false)
        return AlbaranViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AlbaranViewHolder,
        position: Int
    ) {
        val item=albarans[position]
        holder.render(item)
        holder.itemView.setOnClickListener {
            onItemSelected(item)
        }
    }

    override fun getItemCount(): Int {
        return albarans.size
    }
}