package com.cumpatomas.brunosrecipes.domain


import com.cumpatomas.brunosrecipes.data.localdb.Preferences
import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity
import com.cumpatomas.brunosrecipes.data.localdb.entities.toEntity
import com.cumpatomas.brunosrecipes.data.network.RecipeService
import com.cumpatomas.brunosrecipes.data.network.ResponseEvent
import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel
import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule

class SaveRecipesUseCase {

    private val provider = RecipeService()
    private val preferences = Preferences()

    suspend operator fun invoke() {
        when(val result = provider.getRecipes()) {
            is ResponseEvent.Error -> {
                result.exception
            }
            is ResponseEvent.Success -> {
                val localList = LocalDatabaseModule.db.getRecipesDao().getRecipesList()
                val localListNames = localList.map { it.name }
                val recipesList: List<RecipeModel> = result.data
                val recipesListEntity: List<RecipeEntity> = recipesList.map { it.toEntity() }
                if (preferences.isVirgin()) {
                    LocalDatabaseModule.db.getRecipesDao().insertRecipesList(recipesListEntity)
                    preferences.setIsNotVirgin()
                } else {

                    if (recipesListEntity.size != localList.size) {
                        for (i in recipesListEntity) {
                            if (i.name !in localListNames) {
                                LocalDatabaseModule.db.getRecipesDao().insertRecipe(i)
                            } else continue
                        }

                    } else {
                        recipesList.map { recipeModel ->
                            LocalDatabaseModule.db.getRecipesDao().updateRecipes(
                                id = recipeModel.id.toIntOrNull() ?: 0,
                                updatedCategory = recipeModel.category,
                                updatedName = recipeModel.name.orEmpty(),
                                updatedIngredients = recipeModel.ingredients.orEmpty(),
                                updatedPhoto = recipeModel.photo.orEmpty(),
                                updatedPasos = recipeModel.pasos
                            )
                        }
                    }

                }
            }
        }
    }
}