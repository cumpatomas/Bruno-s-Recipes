package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule
import javax.inject.Inject

class DeleteRecipeFromHistory @Inject constructor(private val saveRecipesUseCase: SaveRecipesUseCase) {

    private val dao = LocalDatabaseModule.db.getRecipesDao()
//    val saveRecipesUseCase = SaveRecipesUseCase()

    suspend operator fun invoke(id: Int?, date: String) {
        val recipe = dao.getRecipeById(id)!!
        id ?: return
        if (recipe.datesCooked.size == 1) {
           dao.markRecipeAsCooked(id = id, updatedIsItCooked = false)
            dao.insertNewDateCooked(id = id, newDateCooked = emptyList())
        }
        else {
            val list = recipe.datesCooked.filter { it != date }.toMutableList()
            dao.insertNewDateCooked(id = id, newDateCooked = list)
            dao.markRecipeAsCooked(id = id, updatedIsItCooked = true)

            saveRecipesUseCase.invoke()

        }
    }
}