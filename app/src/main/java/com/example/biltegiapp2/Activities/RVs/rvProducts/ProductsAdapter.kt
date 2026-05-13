package com.example.biltegiapp2.Activities.RVs.rvProducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.DB.Tablak.AkzioMota
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.databinding.ItemProductBinding

class ProductsAdapter (private val products: List<Produktua>, private val updateProduktua: (Produktua, AkzioMota) -> Unit): RecyclerView.Adapter<ProductsViewHolder>() {

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
