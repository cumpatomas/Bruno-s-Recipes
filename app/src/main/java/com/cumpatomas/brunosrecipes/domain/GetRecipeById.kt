package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.domain.model.toDomain
import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule
import javax.inject.Inject

class GetRecipeById@Inject constructor(private val dao: RecipesDao){


    suspend operator fun invoke(id: Int?): RecipesModel? {
       return dao.getRecipeById(id ?: 0)?.toDomain()
    }
}