package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.data.localdb.Preferences
import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity
import com.cumpatomas.brunosrecipes.data.network.RecipeService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.Dispatcher
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class SaveRecipesUseCaseTest {

    @RelaxedMockK
    lateinit var recipeService: RecipeService
    @RelaxedMockK
    lateinit var recipesDao: RecipesDao

    private lateinit var saveRecipes: SaveRecipesUseCase
    private lateinit var preferences: Preferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        saveRecipes = SaveRecipesUseCase(recipeService, recipesDao, preferences)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save recipe`() = runTest {

    }
}