package com.example.biltegiapp2.Activities.RVs.rvProducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.R
class ProductsAdapter (private val products: List<Produktua>): RecyclerView.Adapter<ProductsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductsViewHolder,
        position: Int
    ) {

        holder.render(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }
}