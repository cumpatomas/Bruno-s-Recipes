package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import javax.inject.Inject

class DeleteRecipeFromHistory @Inject constructor(
    private val saveRecipesUseCase: SaveRecipesUseCase,
    private val recipeDao : RecipesDao
    ) {

    suspend operator fun invoke(id: Int?, date: String) {
        val recipe = recipeDao.getRecipeById(id)!!
        id ?: return
        if (recipe.datesCooked.size == 1) {
            recipeDao.markRecipeAsCooked(id = id, updatedIsItCooked = false)
            recipeDao.insertNewDateCooked(id = id, newDateCooked = emptyList())
        }
        else {
            val list = recipe.datesCooked.filter { it != date }.toMutableList()
            recipeDao.insertNewDateCooked(id = id, newDateCooked = list)
            recipeDao.markRecipeAsCooked(id = id, updatedIsItCooked = true)

            saveRecipesUseCase.invoke()

        }
    }
}