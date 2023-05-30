package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import javax.inject.Inject

class GetDatesCooked@Inject constructor(private  val dao: RecipesDao) {


    suspend operator fun invoke() = dao.getRecipesList().map {
        Pair(it.name, it.datesCooked)
    }
}