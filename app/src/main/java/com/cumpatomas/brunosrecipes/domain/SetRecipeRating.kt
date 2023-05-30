package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import javax.inject.Inject

class SetRecipeRating@Inject constructor(
    private val recipesDao: RecipesDao
) {

    suspend operator fun invoke(id: Int?, rating: Float) {
        id ?: return
        recipesDao.insertRecipeRating(id = id, rating = rating )
        val currentRatingFromLocalDB = recipesDao.getLastRating(id)
        currentRatingFromLocalDB.rating = rating
        recipesDao.insertRating(id = id, rating = currentRatingFromLocalDB.rating)
    }
}