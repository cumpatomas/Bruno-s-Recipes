package com.cumpatomas.brunosrecipes.data.network

import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel
import com.cumpatomas.brunosrecipes.manualdi.NetworkModule

class RecipeService {

    private val retrofit = NetworkModule.retrofit.create(RecipesAPI::class.java)

    suspend fun getRecipes(): ResponseEvent<List<RecipeModel>> {
        return try {
//            val response = retrofit.getRecipes("4d513b46-b153-43c5-bd6e-9eed3859aa52") // old Mocky
//            val response = retrofit.getRecipes("6152521f-472e-4e42-9e83-d861106cee32")
//            val response = retrofit.getRecipes("49f72312-2e24-45a4-83bb-c66230bf0824")
            val response = retrofit.getRecipes("7e813516-90d0-4c84-bc75-ad6f2e68f47f")
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