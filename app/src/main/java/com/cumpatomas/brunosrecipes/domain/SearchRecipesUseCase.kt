package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.core.ex.unaccent
import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.domain.model.toDomain
import javax.inject.Inject

private const val EMPTY_STRING = ""

class SearchRecipesUseCase @Inject constructor(
    private val recipesDao: RecipesDao
){

    suspend operator fun invoke(query: String = EMPTY_STRING): List<RecipesModel> {
        val recipesList: List<RecipesModel> =
            recipesDao.getRecipesList().map { it.toDomain() }

        return when (query) {
            EMPTY_STRING -> {
                recipesList
            }

            else -> {
                recipesList.filter {
                    query.lowercase() in it.name.lowercase() || query.lowercase() in it.name.lowercase()
                        .unaccent()
                }
            }
        }
    }
}