package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule

class SetRecipeRating {

    private val dao = LocalDatabaseModule.db.getRecipesDao()

    suspend operator fun invoke(id: Int?, rating: Float) {
        id ?: return
        dao.insertRecipeRating(id = id, rating = rating )
        val currentRatingFromLocalDB = dao.getLastRating(id)
        currentRatingFromLocalDB.rating = rating
        dao.insertRating(id = id, rating = currentRatingFromLocalDB.rating)

    }
}