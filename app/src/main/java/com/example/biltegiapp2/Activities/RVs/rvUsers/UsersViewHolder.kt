package com.example.biltegiapp2.Activities.RVs.rvUsers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.R

class UsersViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val photoUser: ImageView = view.findViewById(R.id.photoUser)
    private val txtUser: TextView = view.findViewById(R.id.txtUser)

    fun render(users: Profila){
        val context= itemView.context
        val resourceId= context.resources.getIdentifier(users.img, "drawable", context.packageName)
        if(resourceId !=0){
            photoUser.setImageResource(resourceId)
        }else{
            photoUser.setImageResource(R.drawable.outline_account_circle_24)
        }
        txtUser.text = users.izena
    }
}