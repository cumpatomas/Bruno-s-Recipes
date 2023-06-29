package com.cumpatomas.brunosrecipes.data

import com.cumpatomas.brunosrecipes.data.network.ResponseEvent
import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel

class LocalRecipeService() {

    fun getRecipes(isError: Boolean): ResponseEvent<List<RecipeModel>> {
        val recipesList = listOf<RecipeModel>(
            RecipeModel(
                id = "1",
                category = listOf(),
                name = "Sasha Sellers",
                ingredients = "suas",
                photo = "himenaeos",
                isItCooked = false,
                rating = 0.1f,
                pasos = "null is dull and bull"
            ),
            RecipeModel(
                id = "2",
                category = listOf(),
                name = "Kathie Lowery",
                ingredients = "tritani",
                photo = "id",
                isItCooked = false,
                rating = 2.3f,
                pasos = "null"
            ),
            RecipeModel(
                id = "3",
                category = listOf(),
                name = "Guillermo Bates",
                ingredients = "malorum",
                photo = "dapibus",
                isItCooked = false,
                rating = 4.5f,
                pasos = "this is not null"
            )
        )

        return if (isError) {
            ResponseEvent.Error(Exception("Failed getting recipes list"))
        } else {
            ResponseEvent.Success(recipesList)
        }
    }

}