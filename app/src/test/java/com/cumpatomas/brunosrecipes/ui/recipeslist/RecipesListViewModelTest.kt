package com.cumpatomas.brunosrecipes.ui.recipeslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cumpatomas.brunosrecipes.domain.SaveRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecipesListViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatchers = StandardTestDispatcher()
    private val searchRecipesUseCase: SearchRecipesUseCase = mockk(relaxed = true)
    private val saveRecipesUseCase: SaveRecipesUseCase = mockk()
    lateinit var viewModel: RecipesListViewModel
    private val recipesList = listOf<RecipesModel>(
        RecipesModel(
            id = 2,
            category = listOf("dulce", "invierno", "verano"),
            name = "Alice Sullivan",
            ingredients = "viverra",
            photo = "neglegentur",
            isItCooked = false,
            datesCooked = mutableListOf(),
            rating = 2.3f,
            pasos = null
        ),
        RecipesModel(
            id = 1,
            category = listOf("salado", "invierno"),
            name = "sopa de Caracoles",
            ingredients = "non",
            photo = "maecenas",
            isItCooked = false,
            datesCooked = mutableListOf(),
            rating = 0.1f,
            pasos = null
        ),
        RecipesModel(
            id = 3,
            category = listOf("dulce", "invierno"),
            name = "Tarta de fresas",
            ingredients = "viverra",
            photo = "neglegentur",
            isItCooked = false,
            datesCooked = mutableListOf(),
            rating = 2.3f,
            pasos = null
        ),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)
        viewModel = RecipesListViewModel(saveRecipesUseCase, searchRecipesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `clearCategoriesSelected function clears list`()  {
        viewModel.addCategoriesSelected("dulce")
        viewModel.addCategoriesSelected("invierno")
        var result = viewModel.categoriesSelected
        assertThat(result.isEmpty()).isFalse()
        viewModel.clearCategoriesSelected()
        result = viewModel.categoriesSelected
        assertThat(result.isEmpty()).isTrue()

    }

    @Test
    fun `searchListTest`() = runBlocking {
        coEvery { searchRecipesUseCase.invoke(any()) } returns recipesList
        viewModel.clearCategoriesSelected()
        viewModel.searchList()
        val result = searchRecipesUseCase.invoke()
        assertThat(result.isEmpty()).isTrue()

    }
  }