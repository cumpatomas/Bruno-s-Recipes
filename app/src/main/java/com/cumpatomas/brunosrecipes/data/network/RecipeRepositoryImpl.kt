package com.cumpatomas.brunosrecipes.data.network

import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel

class RecipeRepositoryImpl: RecipeRepository {


    override suspend fun getRecipes(): ResponseEvent<List<RecipeModel>> {
        TODO("Not yet implemented")
    }


}