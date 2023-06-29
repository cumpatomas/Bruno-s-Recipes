package com.cumpatomas.brunosrecipes.ui.recipeslist

data class RecipesListViewState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: Boolean = false
)