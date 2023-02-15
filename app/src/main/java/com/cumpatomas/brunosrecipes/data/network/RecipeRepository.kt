package com.cumpatomas.brunosrecipes.data.network

import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel

interface RecipeRepository {

    suspend fun getRecipes(): ResponseEvent<List<RecipeModel>>

}