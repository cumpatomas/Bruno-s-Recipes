package com.cumpatomas.brunosrecipes.ui.input

import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class InputComposeViewModelTest {
    private val dispatchers = StandardTestDispatcher()
    private val searchRecipesUseCase: SearchRecipesUseCase = mockk()
    private lateinit var viewModel: InputComposeViewModel
    private val recipesList = listOf<RecipesModel>(
        RecipesModel(
            id = 2,
            category = listOf("dulce", "invierno", "verano"),
            name = "Alice Sullivan",
            ingredients = "queso, tomate, lechuga",
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
            ingredients = "queso, atún, harina",
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
            ingredients = "fresas, nata, harina",
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
        viewModel = InputComposeViewModel(searchRecipesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check init ingredientsList value`() = runTest {
        val ingredientes = viewModel.ingredientsList.value
        assertThat(ingredientes.size == 7).isTrue()
    }

    @Test
    fun `check selectedIngredients function`() = runTest {
        val selectedIngredients = viewModel.selectedIngredientsList
        assertThat(selectedIngredients.isEmpty()).isTrue()
        viewModel.selectedIngredients("huevos")
        viewModel.selectedIngredients("nata")
        viewModel.selectedIngredients("harina")
        viewModel.selectedIngredients("fresas")
        advanceUntilIdle()
        assertThat(selectedIngredients.size == 4).isTrue()
        val posibleRecipes = viewModel.posibleRecipesList.value
        assertThat(posibleRecipes.size == 1).isTrue()
        assertThat(viewModel.posibleRecipesNumber.value == 1).isTrue()
    }
}