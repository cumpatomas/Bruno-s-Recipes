package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.manualdi.LocalDatabaseModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MarkRecipeCookedUseCase {

    private val dao = LocalDatabaseModule.db.getRecipesDao()

    suspend operator fun invoke(id: Int?) {
        id ?: return
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val date = LocalDateTime.now().format(dateFormat).toString()
        dao.markRecipeAsCooked(id = id, updatedIsItCooked = true)
        val currentCookedDatesFromLocalDB = dao.getLastDatesCooked(id)
        currentCookedDatesFromLocalDB.datesCooked.add(date)
        dao.insertNewDateCooked(id = id, newDateCooked = currentCookedDatesFromLocalDB.datesCooked)
    }
}