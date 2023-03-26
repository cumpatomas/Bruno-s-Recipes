package com.cumpatomas.brunosrecipes.data.network

import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel
import com.cumpatomas.brunosrecipes.manualdi.NetworkModule

class RecipeService {

    private val retrofit = NetworkModule.retrofit.create(RecipesAPI::class.java)

    suspend fun getRecipes(): ResponseEvent<List<RecipeModel>> {
        return try {
//            val response = retrofit.getRecipes("4d513b46-b153-43c5-bd6e-9eed3859aa52") // old Mocky
val response = retrofit.getRecipes("db346a2c-7d7c-4d29-89f4-4568860762f3")
            if (response.isSuccessful) {
                response.body()?.let { recipeList ->
                    ResponseEvent.Success(recipeList)
                } ?: run {
                    ResponseEvent.Error(Exception("Response body is null."))
                }
            } else {
                ResponseEvent.Error(Exception("Get recipes not succesfull."))
            }
        } catch (e: Exception) {
            ResponseEvent.Error(e)
        }
    }

}