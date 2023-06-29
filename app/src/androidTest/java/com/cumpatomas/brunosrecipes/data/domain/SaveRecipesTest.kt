package com.cumpatomas.brunosrecipes.data.domain

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.cumpatomas.brunosrecipes.data.localdb.LocalDatabase
import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity
import com.cumpatomas.brunosrecipes.data.localdb.entities.toEntity
import com.cumpatomas.brunosrecipes.data.LocalRecipeService
import com.cumpatomas.brunosrecipes.data.network.ResponseEvent
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@SmallTest
class SaveRecipesTest {
    private lateinit var recipeService: LocalRecipeService
    private lateinit var database: LocalDatabase
    private lateinit var dao: RecipesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java
        ).allowMainThreadQueries().build()

        recipeService = LocalRecipeService()
        dao = database.getRecipesDao()
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun saveRecipesSuccess() {
        when (val result = recipeService.getRecipes(false)) {
            is ResponseEvent.Error -> {
                result.exception
            }

            is ResponseEvent.Success -> {
                val recipesListEntity: List<RecipeEntity> = result.data.map { it.toEntity() }
                runBlocking {
                    dao.insertRecipesList(recipesListEntity)
                }
            }
        }
        var getRecipes = listOf<RecipeEntity>()
        runBlocking {
            getRecipes = database.getRecipesDao().getRecipesList()
        }
        assertThat(getRecipes.isNotEmpty()).isTrue()
    }
}