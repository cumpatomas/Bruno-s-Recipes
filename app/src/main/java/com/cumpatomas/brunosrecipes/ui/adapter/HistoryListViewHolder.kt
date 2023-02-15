package com.cumpatomas.brunosrecipes.ui.adapter

import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.databinding.HistoryItemBinding
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel

class HistoryListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = HistoryItemBinding.bind(view)
    fun display(recipe: RecipesModel, onClickListener: (Int) -> Unit) {
        binding.tvHistoryDate.text = recipe.datesCooked.last()
        binding.tvHistoryRecipe.text = recipe.name
        Glide.with(binding.ivHistoryImage.context)
            .load(recipe.photo)
            .apply(RequestOptions().transform(CircleCrop(), RoundedCorners(15)))
            .into(binding.ivHistoryImage)

        itemView.setOnClickListener { onClickListener(recipe.id) }
        setAnimation()
    }

    private fun setAnimation() {
        val animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.slide_in_left)
        itemView.startAnimation(animation)
    }

}