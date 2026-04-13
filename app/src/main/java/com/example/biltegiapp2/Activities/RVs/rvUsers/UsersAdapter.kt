package com.example.biltegiapp2.Activities.RVs.rvUsers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.RVs.rvUsers.UsersViewHolder
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.R

class UsersAdapter(private val users: List<Profila>, private val onItemSelected: (Profila) -> Unit): RecyclerView.Adapter<UsersViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UsersViewHolder,
        position: Int
    ) {
        val item=users[position]
        holder.render(item)
        holder.itemView.setOnClickListener {
            onItemSelected(item)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}