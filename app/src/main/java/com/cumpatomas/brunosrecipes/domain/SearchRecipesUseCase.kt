package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.core.ex.unaccent
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.domain.model.toDomain
import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule

private const val EMPTY_STRING = ""

class SearchRecipesUseCase {

    suspend operator fun invoke(query: String = EMPTY_STRING): List<RecipesModel> {
        val recipesList: List<RecipesModel> =
            LocalDatabaseModule.db.getRecipesDao().getRecipesList().map { it.toDomain() }

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