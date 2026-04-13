package com.example.biltegiapp2.Activities.RVs.rvProducts


import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.AppUtils
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.databinding.ItemProductBinding
import androidx.core.content.ContextCompat
import android.graphics.Color

class ProductsViewHolder(private val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
    val context = binding.root.context

    fun render(products: Produktua, updateProduktua: (Produktua) -> Unit){
        if(products.kantitatea<=products.gutxienekoKantitatea){
            val border = 9* context.resources.displayMetrics.density
            binding.imgAliment.strokeWidth= border
            binding.imgAliment.strokeColor = android.content.res.ColorStateList.valueOf(Color.RED)
        }else{
            binding.imgAliment.strokeWidth = 0f
        }

        AppUtils.uploadImg(binding.imgAliment, products.img)
        binding.txtProduct.setText(products.izena)

        binding.txtQuatity.setText(products.kantitatea.toString())
        binding.btnPlus.setOnClickListener {
            products.kantitatea++
            binding.txtQuatity.setText(products.kantitatea.toString())
            updateProduktua(products)
        }
        binding.btnMinus.setOnClickListener {
            products.kantitatea--
            binding.txtQuatity.setText(products.kantitatea.toString())
            updateProduktua(products)
        }
    }
}