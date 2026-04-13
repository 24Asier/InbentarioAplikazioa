package com.example.biltegiapp2.Activities.RVs.rvEditProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.RVs.rvUsers.UsersViewHolder
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.R

class EditProductAdapter(private val products: List<Produktua>, private val onItemSelected: (Produktua) -> Unit): RecyclerView.Adapter<EditProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return EditProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EditProductViewHolder,
        position: Int
    ) {
        val item=products[position]
        holder.render(item)
        holder.itemView.setOnClickListener {
            onItemSelected(item)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}