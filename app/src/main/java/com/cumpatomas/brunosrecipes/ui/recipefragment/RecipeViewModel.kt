package com.cumpatomas.brunosrecipes.ui.recipefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.brunosrecipes.domain.GetRecipeById
import com.cumpatomas.brunosrecipes.domain.MarkRecipeCookedUseCase
import com.cumpatomas.brunosrecipes.domain.SetRecipeRating
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel@Inject constructor(
    private val getRecipeById : GetRecipeById,
    private val markRecipeCookedUseCase : MarkRecipeCookedUseCase,
    private val setRecipeRating : SetRecipeRating
) : ViewModel() {

    var id: Int? = null
        private set

    private val _recipe = MutableStateFlow<RecipesModel?>(null)
    val recipe = _recipe.asStateFlow()
    fun setRecipeRating(stars: Float) {

        viewModelScope.launch {
            setRecipeRating.invoke(this@RecipeViewModel.id, stars)
            _recipe.value = getRecipeById.invoke(this@RecipeViewModel.id)

        }
    }

    fun receiveArguments(id: Int) {
        this.id = id
        viewModelScope.launch {
            _recipe.value = getRecipeById.invoke(this@RecipeViewModel.id)
        }
    }

    fun markRecipeAsCooked() {
        viewModelScope.launch {
            markRecipeCookedUseCase.invoke(id)
            _recipe.value = getRecipeById.invoke(this@RecipeViewModel.id)
        }
    }
}
