package com.example.biltegiapp2.Activities.RVs.rvEditProduct

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.AppUtils
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.R

class EditProductViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val photoUser: ImageView = view.findViewById(R.id.photoUser)
    private val txtUser: TextView = view.findViewById(R.id.txtUser)

    fun render(products: Produktua){
        val context= itemView.context
        AppUtils.uploadImg(photoUser, products.img, "aliment")
        txtUser.text= products.izena
    }
}