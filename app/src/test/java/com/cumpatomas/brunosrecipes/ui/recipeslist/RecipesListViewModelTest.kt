package com.cumpatomas.brunosrecipes.ui.recipeslist

import app.cash.turbine.test
import com.cumpatomas.brunosrecipes.domain.SaveRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecipesListViewModelTest {
/*    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()*/
    private val dispatchers = StandardTestDispatcher()
    private val searchRecipesUseCase: SearchRecipesUseCase = mockk()
    private val saveRecipesUseCase: SaveRecipesUseCase = mockk(relaxed = true)
    private lateinit var viewModel: RecipesListViewModel
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
        coEvery { searchRecipesUseCase.invoke() } returns recipesList
        viewModel = RecipesListViewModel(saveRecipesUseCase, searchRecipesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `clearCategoriesSelected function clears list`() {
        viewModel.addCategoriesSelected("dulce")
        viewModel.addCategoriesSelected("invierno")
        var result = viewModel.categoriesSelected
        assertThat(result.isEmpty()).isFalse()
        viewModel.clearCategoriesSelected()
        result = viewModel.categoriesSelected
        assertThat(result.isEmpty()).isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test updateRecipesList function`() = runTest {

        viewModel.recipesList.test() {
            var result = awaitItem()
            assertThat(result.size == 3).isTrue()
            coEvery { searchRecipesUseCase.invoke("Tarta") } returns recipesList.filter { it.name.contains("Tarta") }
            /** in order to successfully test the viewModel.searchList wich uses IO dispatchers
             * I had to create a Dispatchers.IO context scope and also write the assertion INSIDE this block*/
            withContext(Dispatchers.IO) {
                viewModel.searchList("Tarta")
                advanceUntilIdle() // or we can also use launch {viewModel.searchList("Tarta")}.join in the previous line
                result = awaitItem()
                assertThat(result.size == 1).isTrue()
            }
        }
    }

    @Test
    fun `test categoryList after Init`() = runTest {

       /**  IN ORDER TO SUCCEED THIS TEST, I HAD TO PUT THE VIEWSTATE FLOW.SEND CHANNEL
        * IN SEPARATES THREADS USING launch{} OTHERWISE IT GETS STUCK IN THE SEND FLOW */

        viewModel.categoryList.test() {
            withContext(Dispatchers.IO) {
                val result = awaitItem()
                assertThat(result.size == 4).isTrue() // Dulce, invierno, salado, verano from fake recipeList
            }
        }
    }
}