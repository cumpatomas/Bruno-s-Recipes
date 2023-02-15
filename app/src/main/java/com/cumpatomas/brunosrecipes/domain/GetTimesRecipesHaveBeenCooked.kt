package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.domain.model.toDomain
import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class GetTimesRecipesHaveBeenCooked {

    suspend operator fun invoke() = channelFlow<List<RecipesModel>> {
        LocalDatabaseModule.db.getRecipesDao().getRecipesListFlow().collectLatest { recipesEntity ->
            val recipesModel = recipesEntity.map { it.toDomain() }
            val cookedDateList = mutableListOf<RecipesModel>()
            recipesModel.forEach { recipe ->
                for (date in recipe.datesCooked) {
                    val tempModel = RecipesModel(recipe.id, recipe.category, recipe.name, recipe.ingredients, recipe.photo, recipe.isItCooked, listOf(date), recipe.rating, recipe.pasos)
                    cookedDateList.add(tempModel)
                }
            }
            send(cookedDateList)
        }
    }
}