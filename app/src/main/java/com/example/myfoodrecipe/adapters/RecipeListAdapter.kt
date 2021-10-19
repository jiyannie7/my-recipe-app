package com.example.myfoodrecipe.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodrecipe.R
import com.example.myfoodrecipe.data.Recipe

class RecipeListAdapter(private val list: ArrayList<Recipe>) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvName : TextView = view.findViewById(R.id.tv_name)
        val tvType : TextView = view.findViewById(R.id.tv_type)
        val tvIngredients : TextView = view.findViewById(R.id.tv_ingredient)
        val tvSteps : TextView = view.findViewById(R.id.tv_steps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder((LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_list, parent, false)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.tvType.text = list[position].type
        holder.tvName.text = list[position].name
        holder.tvIngredients.text = list[position].ingredients
        holder.tvSteps.text = list[position].steps
    }

    override fun getItemCount(): Int {
        return list.size
    }


}