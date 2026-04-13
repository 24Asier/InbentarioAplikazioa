package com.example.biltegiapp2.Activities.RVs.rvProducts

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.RVs.rvEditProduct.EditProductViewHolder
import com.example.biltegiapp2.DB.Datubasea


import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.ItemProductBinding

class ProductsAdapter (private val products: List<Produktua>, private val updateProduktua: (Produktua) -> Unit): RecyclerView.Adapter<ProductsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductsViewHolder,
        position: Int
    ) {
        val item=products[position]
        holder.render(item, updateProduktua)

    }

    override fun getItemCount(): Int {
        return products.size
    }
}