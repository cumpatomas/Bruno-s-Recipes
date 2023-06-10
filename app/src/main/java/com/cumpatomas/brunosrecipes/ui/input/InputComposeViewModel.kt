package com.cumpatomas.brunosrecipes.ui.input

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputComposeViewModel@Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase
) : ViewModel() {

    val selectedIngredientsList = mutableListOf<String>()
    val selectedIngredientsListState = mutableStateOf(true)
    val posibleRecipesNumber = mutableStateOf<Int>(0)
    var posibleRecipesList = mutableStateOf<List<RecipesModel>>(emptyList())
    val isLoadingState: MutableState<Boolean> = mutableStateOf(true)
    val ingredientsList: MutableState<List<String>> = mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            searchRecipesUseCase.invoke().also { recipeList ->
                isLoadingState.value = true

                val tempList = recipeList.toMutableList()
                val splittedIngredients = mutableListOf<String>()

                tempList.forEach { recipe ->
                    for (ingredient in recipe.ingredients.split(", "))
                        if (ingredient !in splittedIngredients && ingredient.isNotBlank())
                            splittedIngredients.add(ingredient)
                }
                ingredientsList.value = splittedIngredients.sorted()
                isLoadingState.value = false
            }
        }

    }

    fun selectedIngredients(ingredient: String) {


        if (ingredient in selectedIngredientsList) {
            selectedIngredientsList.remove(ingredient)
        } else {
            selectedIngredientsList.add(ingredient)
        }
        viewModelScope.launch {
            val job = launch {
                searchRecipesUseCase.invoke().also { recipeList ->
                    posibleRecipesList.value =
                        recipeList.filter {
                            selectedIngredientsList.containsAll(
                                it.ingredients.split(
                                    ", "
                                )
                            )
                        }
                }
                posibleRecipesNumber.value = posibleRecipesList.value.size

                selectedIngredientsListState.value = selectedIngredientsList.isEmpty()
            }
        }
    }
}

