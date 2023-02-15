package com.cumpatomas.brunosrecipes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel

class RecipeListAdapter : RecyclerView.Adapter<RecipeListViewHolder>() {

    private var list = mutableListOf<RecipesModel>()

    var onItemClickListener: (Int) -> Unit = {}

    fun setList(list: List<RecipesModel>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecipeListViewHolder(layoutInflater.inflate(R.layout.recipe_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val item = list[position]
        holder.display(item, onItemClickListener)
    }

    override fun getItemCount(): Int = list.size

}