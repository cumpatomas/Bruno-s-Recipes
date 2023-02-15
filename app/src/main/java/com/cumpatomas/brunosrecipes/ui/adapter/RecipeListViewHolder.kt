package com.cumpatomas.brunosrecipes.ui.adapter

import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.databinding.RecipeItemBinding
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel


class RecipeListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = RecipeItemBinding.bind(view)

    fun display(recipe: RecipesModel, onClickListener: (Int) -> Unit) {
        binding.tvItemName.text = recipe.name
        //binding.tvItemIngredients.text = recipe.ingredients
        Glide.with(binding.ivItemImage.context)
            .load(recipe.photo)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
            .into(binding.ivItemImage)
        //binding.tvIsCooked.isVisible = recipe.isItCooked
        binding.ivCookedIcon.isVisible = recipe.isItCooked
        binding.ratingBar.rating = recipe.rating
        itemView.setOnClickListener { onClickListener(recipe.id) }
        setAnimation()
    }

    private fun setAnimation() {
        val animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.slide_in_left)
        itemView.startAnimation(animation)
    }
}