package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule

class GetDatesCooked {

    private val dao = LocalDatabaseModule.db.getRecipesDao()

    suspend operator fun invoke() = dao.getRecipesList().map {
        Pair(it.name, it.datesCooked)
    }
}