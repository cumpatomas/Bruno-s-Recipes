package com.cumpatomas.brunosrecipes.ui.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cumpatomas.brunosrecipes.databinding.RecipeItemBinding
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel

class FilteredListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = RecipeItemBinding.bind(view)

    fun display(item: RecipesModel, onItemClickListener: (Int) -> Unit) {

        binding.tvItemName.text = item.name
        //binding.tvItemIngredients.text = recipe.ingredients
        Glide.with(binding.ivItemImage.context)
            .load(item.photo)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
            .into(binding.ivItemImage)
        //binding.tvIsCooked.isVisible = recipe.isItCooked
       binding.ivCookedIcon.isVisible = item.isItCooked

        binding.ratingBar.rating = item.rating

        itemView.setOnClickListener { onItemClickListener(item.id) }



    }


}