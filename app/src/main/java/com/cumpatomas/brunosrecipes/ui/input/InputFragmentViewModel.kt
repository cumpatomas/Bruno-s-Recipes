package com.cumpatomas.brunosrecipes.ui.input

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.cumpatomas.brunosrecipes.domain.SaveRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.ui.recipeslist.RecipesListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class InputFragmentViewModel : ViewModel() {

    private val _selectedIngredients = mutableSetOf<String>()
    private val saveRecipesUseCase = SaveRecipesUseCase()
    private val _selIngredientsFlow = MutableStateFlow<Set<String>>(emptySet())
    val selIngredientsFlow = _selIngredientsFlow.asStateFlow()
    private val searchRecipesUseCase = SearchRecipesUseCase()
    private val _inputRecipesList = MutableStateFlow<List<RecipesModel>>(emptyList())
    val inputRecipesList = _inputRecipesList.asStateFlow()
    private val _ingredientsList = MutableStateFlow<List<String>>(emptyList())
    val ingredientsList = _ingredientsList.asStateFlow()
    private val _viewState = Channel<RecipesListViewState>()
    val viewState = _viewState.receiveAsFlow()
    private val _categoryList = MutableStateFlow<List<String>>(emptyList())
    val categoryList = _categoryList.asStateFlow()
    val ingredientsListTemp: MutableState<List<String>> = mutableStateOf(listOf("default"))

    init {
        viewModelScope.launch(IO) {
            _viewState.send(RecipesListViewState(loading = true))

            val saveRecipesJob = launch {
                saveRecipesUseCase.invoke()
            }
            saveRecipesJob.join()
            searchRecipesUseCase.invoke().also { recipeList ->

                val tempList = recipeList.toMutableList()
                val splittedIngredients = mutableListOf<String>()

                tempList.forEach { recipe ->
                    for (ingredient in recipe.ingredients.split(", "))
                        if (ingredient !in splittedIngredients && ingredient.isNotBlank())
                            splittedIngredients.add(ingredient)
                }

                _ingredientsList.value = splittedIngredients.sorted()
                ingredientsListTemp.value = splittedIngredients.sorted()

                _viewState.send(RecipesListViewState(loading = false))
            }
        }
    }

    fun clearIngredient(ingredient: String) {
        _selectedIngredients.remove(ingredient)
        _selIngredientsFlow.value = _selectedIngredients
    }

    fun addIngredientSelected(ingredient: String) {
        _selectedIngredients.add(ingredient)
        _selIngredientsFlow.value = _selectedIngredients
    }

    fun searchList() {
        viewModelScope.launch(IO) {

            _inputRecipesList.value = searchRecipesUseCase.invoke().filter { recipe ->
                _selectedIngredients.containsAll(recipe.ingredients.split(", "))
            }
        }
    }

    fun clearFilteredRecipesList() {
        _inputRecipesList.value = emptyList()
        _selectedIngredients.clear()
    }
}