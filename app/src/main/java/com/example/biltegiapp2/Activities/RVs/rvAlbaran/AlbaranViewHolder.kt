package com.example.biltegiapp2.Activities.RVs.rvAlbaran

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.AppUtils
import com.example.biltegiapp2.DB.Tablak.Albaran
import com.example.biltegiapp2.R

class AlbaranViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val photoAlbaran: ImageView= view.findViewById(R.id.photoAlbaran)
    private val txtAlbaran : TextView= view.findViewById(R.id.txtAlbaran)

    fun render(albaran: Albaran){
        AppUtils.uploadImg(photoAlbaran, albaran.img)
        txtAlbaran.setText(albaran.izena)
    }


}