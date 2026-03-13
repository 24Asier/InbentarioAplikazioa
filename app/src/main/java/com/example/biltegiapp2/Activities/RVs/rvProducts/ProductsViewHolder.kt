package com.example.biltegiapp2.Activities.RVs.rvProducts

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.R

class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val imgAliment: ImageView= view.findViewById(R.id.imgAliment)

    fun render(products: Produktua){
        val context= itemView.context
        val resourceId= context.resources.getIdentifier(products.img, "drawable", context.packageName)
        if(resourceId !=0){
            imgAliment.setImageResource(resourceId)
        }else{
            imgAliment.setImageResource(R.drawable.aliment)
        }
    }
}