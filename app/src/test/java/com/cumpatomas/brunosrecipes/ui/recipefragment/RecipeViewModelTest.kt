package com.cumpatomas.brunosrecipes.ui.recipefragment

import app.cash.turbine.test
import com.cumpatomas.brunosrecipes.domain.GetRecipeById
import com.cumpatomas.brunosrecipes.domain.MarkRecipeCookedUseCase
import com.cumpatomas.brunosrecipes.domain.SetRecipeRating
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecipeViewModelTest {
    private val getRecipeById: GetRecipeById = mockk()
    private val markRecipeCookedUseCase: MarkRecipeCookedUseCase = mockk()
    private val setRecipeRating: SetRecipeRating = mockk()
    private val dispatchers = StandardTestDispatcher()
    lateinit var viewModel: RecipeViewModel
    private val recipe = RecipesModel(
        id = 2,
        category = listOf("dulce", "invierno", "verano"),
        name = "Alice Sullivan",
        ingredients = "queso, tomate, lechuga",
        photo = "neglegentur",
        isItCooked = false,
        datesCooked = mutableListOf(),
        rating = 2.3f,
        pasos = null
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)
        coEvery { getRecipeById.invoke(any()) } returns recipe
        viewModel = RecipeViewModel(
            getRecipeById,
            markRecipeCookedUseCase,
            setRecipeRating
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check recipe value after receiveArguments function`() = runTest {
        viewModel.recipe.test {
            var recipe = awaitItem()
            viewModel.receiveArguments(2)
            recipe = awaitItem()
            assertThat(recipe?.rating == 2.3f).isTrue()
            assertThat(recipe?.name == "Alice Sullivan").isTrue()
        }
    }

    /** setRecipeRating and markRecipeAsCooked function must be TESTED as instrumented
     * because both refer to usecases related to the DB */
}