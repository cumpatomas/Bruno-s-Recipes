package com.cumpatomas.brunosrecipes.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert
    suspend fun insertRecipesList(recipesList: List<RecipeEntity>)

    @Query("SELECT * FROM recipes_entity")
    suspend fun getRecipesList(): List<RecipeEntity>

    @Query("SELECT * FROM recipes_entity")
    fun getRecipesListFlow(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes_entity WHERE id = :id")
    suspend fun getRecipeById(id: Int?): RecipeEntity?

    @Query("UPDATE recipes_entity SET category = :updatedCategory, name = :updatedName, ingredients = :updatedIngredients, photo = :updatedPhoto, pasos = :updatedPasos WHERE id = :id")
    suspend fun  updateRecipes(id: Int, updatedCategory: List<String>, updatedName: String, updatedIngredients: String, updatedPhoto: String, updatedPasos: String )

    @Query("UPDATE recipes_entity SET isItCooked = :updatedIsItCooked WHERE id = :id")
    suspend fun markRecipeAsCooked(id: Int, updatedIsItCooked: Boolean)

    @Query("SELECT datesCooked, category, name, ingredients, photo, isItCooked, isFavorite, rating, pasos FROM recipes_entity WHERE id = :id")
    suspend fun getLastDatesCooked(id: Int): RecipeEntity

    @Query("UPDATE recipes_entity SET datesCooked = :newDateCooked WHERE id = :id")
    suspend fun insertNewDateCooked(id: Int, newDateCooked: List<String>)

    @Query("UPDATE recipes_entity SET rating = :rating WHERE id = :id")
    suspend fun insertRecipeRating(id: Int, rating: Float)

    @Query("SELECT datesCooked, category, name, ingredients, photo, isItCooked, isFavorite, rating, pasos FROM recipes_entity WHERE id = :id")
    suspend fun getLastRating(id: Int): RecipeEntity

    @Query("UPDATE recipes_entity SET rating = :rating WHERE id = :id")
    suspend fun insertRating(id: Int, rating: Float)


    @Query("DELETE FROM recipes_entity")
    fun deleteAllRecipes()



}