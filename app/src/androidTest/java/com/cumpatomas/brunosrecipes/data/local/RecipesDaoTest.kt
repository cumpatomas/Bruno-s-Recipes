package com.cumpatomas.brunosrecipes.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.cumpatomas.brunosrecipes.data.localdb.LocalDatabase
import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@SmallTest
class RecipesDaoTest {

    private lateinit var database: LocalDatabase
    private lateinit var dao: RecipesDao
    private lateinit var recipeExample: RecipeEntity
    private lateinit var recipesList: List<RecipeEntity>


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.getRecipesDao()
        recipeExample = RecipeEntity(
            id = 1,
            category = listOf(),
            name = "sopa de Caracoles",
            ingredients = "non",
            photo = "maecenas",
            isFavorite = false,
            isItCooked = false,
            datesCooked =mutableListOf(),
            rating = 0.1f,
            pasos = null
        )

         recipesList = listOf<RecipeEntity>(
            RecipeEntity(
                id = 2,
                category = listOf(),
                name = "Alice Sullivan",
                ingredients = "viverra",
                photo = "neglegentur",
                isFavorite = false,
                isItCooked = false,
                datesCooked = mutableListOf(),
                rating = 2.3f,
                pasos = null
            ),
             RecipeEntity(
                 id = 1,
                 category = listOf(),
                 name = "sopa de Caracoles",
                 ingredients = "non",
                 photo = "maecenas",
                 isFavorite = false,
                 isItCooked = false,
                 datesCooked =mutableListOf(),
                 rating = 0.1f,
                 pasos = null
             ),
            RecipeEntity(
                id = 3,
                category = listOf(),
                name = "Tarta de fresas",
                ingredients = "viverra",
                photo = "neglegentur",
                isFavorite = false,
                isItCooked = false,
                datesCooked = mutableListOf(),
                rating = 2.3f,
                pasos = null
            ),
        )
    }



    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertRecipe() = runTest {

        dao.insertRecipesList(recipesList)

        val getRecipes = dao.getRecipesList()

        Truth.assertThat(getRecipes).contains(recipeExample)
    }
@Test
    fun getRecipeById() = runTest {

        dao.insertRecipesList(recipesList)

        val recipeById = dao.getRecipeById(3)

        Truth.assertThat(recipeById?.name).isEqualTo("Tarta de fresas")
    }

    @Test
    fun markRecipeAsCooked() = runTest {
        dao.insertRecipesList(recipesList)

        dao.markRecipeAsCooked(3, true)
        val recipedMarkedAsCooked = dao.getRecipeById(3)

        assertThat(recipedMarkedAsCooked?.isItCooked).isTrue()
    }

    @Test
    fun deleteAllRecipes() = runTest {
        dao.insertRecipesList(recipesList)

        dao.deleteAllRecipes()

        val getRecipes = dao.getRecipesList()

        assertThat(getRecipes).isEmpty()
    }
}