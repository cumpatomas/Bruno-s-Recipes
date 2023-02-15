package com.cumpatomas.brunosrecipes.data.network

import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RecipesAPI {

    @GET
    suspend fun getRecipes(
        @Url url: String
    ): Response<List<RecipeModel>>
}