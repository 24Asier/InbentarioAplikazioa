package com.example.biltegiapp2.Activities.RVs.rvProducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.ItemProductInventoryBinding

class InventoryProductsAdapter (private val products: List<Produktua>): RecyclerView.Adapter<InventoryProductsAdapter.InventoryProductViewHolder>() {

    inner class InventoryProductViewHolder(val bindingItem: ItemProductInventoryBinding): RecyclerView.ViewHolder(bindingItem.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryProductViewHolder {
        val binding = ItemProductInventoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return InventoryProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: InventoryProductViewHolder,
        position: Int
    ) {
        val item = products[position]

        holder.bindingItem.txtProduct.text = item.izena
        holder.bindingItem.txtQuatity.text = item.kantitatea.toString()

        val context = holder.itemView.context
        val imageId = context.resources.getIdentifier(item.img, "drawable", context.packageName)
        if (imageId != 0) {
            holder.bindingItem.imgAliment.setImageResource(imageId)
        } else {
            holder.bindingItem.imgAliment.setImageResource(R.drawable.aliment)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}
