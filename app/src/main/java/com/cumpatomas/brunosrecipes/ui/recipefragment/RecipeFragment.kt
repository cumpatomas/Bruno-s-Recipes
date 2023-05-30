package com.cumpatomas.brunosrecipes.ui.recipefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.databinding.RecipeFragmentBinding
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.ui.adapter.HistoryListAdapter
import com.cumpatomas.brunosrecipes.ui.history.HistoryFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding!!
    private val navArgs: RecipeFragmentArgs by navArgs()
    private val viewModel: RecipeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        receiveArguments()
        setUpRatingBar()
        initCollectors()
        initListeners()
    }

    private fun setUpRatingBar() {

        val ratingBarStars: RatingBar = binding.ratingStars
        ratingBarStars.setOnRatingBarChangeListener { _, rating, _ ->
            setRecipeRating(rating)

        }
    }

    private fun setRecipeRating(rating: Float) {
        viewModel.setRecipeRating(rating)
    }


    private fun initListeners() {
        binding.btnMarkedAsCooked.setOnClickListener {
            viewModel.markRecipeAsCooked()
            binding.btnMarkedAsCooked.isEnabled = false
            binding.btnMarkedAsCooked.alpha = 0.5f
        }
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.recipe.collectLatest { recipeModel ->
                        updateUiWithRecipeInfo(recipeModel)
                        updateLastDateCooked(recipeModel)
                        binding.ratingStars.rating = recipeModel?.rating ?: 0f

                    }
                }
            }
        }
    }


    private fun updateLastDateCooked(recipeModel: RecipesModel?) {
        if (recipeModel?.datesCooked?.isEmpty() == true) {
            binding.tvLastDateText.text = resources.getText(R.string.havent_try)
        } else
            binding.tvLastDateText.text = recipeModel?.datesCooked?.last().toString()
    }

    private fun updateUiWithRecipeInfo(recipeModel: RecipesModel?) {
        Glide.with(requireContext())
            .load(recipeModel?.photo)
            .apply(RequestOptions().transform(CircleCrop()))
            .into(binding.ivRecipeImage)

        binding.tvName.text = recipeModel?.name
        binding.tvCategories.text = recipeModel?.category?.joinToString(", ")
        binding.tvIngredients.text = recipeModel?.ingredients
        binding.tvPasosTexto.text = recipeModel?.pasos?.trimIndent()

    }

    private fun receiveArguments() {
        viewModel.receiveArguments(navArgs.recipeId)
    }
}