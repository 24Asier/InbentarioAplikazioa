package com.example.biltegiapp2.Activities.RVs.rvProducts

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.DB.Datubasea


import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.ItemProductBinding

class ProductsAdapter (private val products: List<Produktua>, private val updateProduktua:(Produktua) -> Unit): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val bindingItem: ItemProductBinding): RecyclerView.ViewHolder(bindingItem.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),parent
            , false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val item= products[position]



        holder.bindingItem.txtProduct.setText(item.izena)
        val context =holder.itemView.context
        val imageId=context.resources.getIdentifier(item.img, "drawable", context.packageName)
        if(imageId!=0){
            holder.bindingItem.imgAliment.setImageResource(imageId)
        }else{
            holder.bindingItem.imgAliment.setImageResource(R.drawable.aliment)

        }
        holder.bindingItem.txtQuatity.setText(item.kantitatea.toString())
        holder.bindingItem.btnPlus.setOnClickListener {
            item.kantitatea++
            holder.bindingItem.txtQuatity.setText(item.kantitatea.toString())
            updateProduktua(item)
        }
        holder.bindingItem.btnMinus.setOnClickListener {
            item.kantitatea--
            holder.bindingItem.txtQuatity.setText(item.kantitatea.toString())
            updateProduktua(item)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}