package com.cumpatomas.brunosrecipes.ui.recipefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel
import com.cumpatomas.brunosrecipes.domain.GetRecipeById
import com.cumpatomas.brunosrecipes.domain.MarkRecipeCookedUseCase
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.SetRecipeRating
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule
import com.cumpatomas.brunosrecipes.ui.adapter.HistoryListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Interceptor.Companion.invoke


class RecipeViewModel : ViewModel() {

    private val getRecipeById = GetRecipeById()
    private val markRecipeCookedUseCase = MarkRecipeCookedUseCase()
    private val setRecipeRating = SetRecipeRating()


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
