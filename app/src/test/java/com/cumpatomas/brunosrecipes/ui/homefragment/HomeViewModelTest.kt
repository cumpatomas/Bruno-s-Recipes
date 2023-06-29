package com.cumpatomas.brunosrecipes.ui.homefragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.mutableStateOf
import com.cumpatomas.brunosrecipes.domain.SaveRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.ScrapNews
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.NewsModel
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatchers = StandardTestDispatcher()
    private val searchRecipesUseCase: SearchRecipesUseCase = mockk()
    private val saveRecipesUseCase: SaveRecipesUseCase = mockk(relaxed = true)
    private val scrapNews: ScrapNews = mockk(relaxed = true)
    lateinit var viewModel: HomeViewModel
    private val newsList = listOf(
        NewsModel(id = null, title = "vidisse", link = "turpis", state = mutableStateOf(true)),
        NewsModel(id = null, title = "new new", link = "http...etc", state = mutableStateOf(true)),
        NewsModel(
            id = null, title = "this is another new", link = "example", state = mutableStateOf(true)
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatchers)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `closeNewsCard change all news elements field state to false`() = runBlocking {
        viewModel = HomeViewModel(searchRecipesUseCase, saveRecipesUseCase, scrapNews)
        viewModel.newsList.value = newsList
        viewModel.closeNewsCard()
        for (new in viewModel.newsList.value) {
            assertThat(new.state.value).isFalse()
        }
    }

    @Test
    fun `getBestRatedRecipes filters recipes with more than 2 starts and order them`() =
        runBlocking {
            val recipesList = listOf<RecipesModel>(
                RecipesModel(
                    id = 2,
                    category = listOf(),
                    name = "Alice Sullivan",
                    ingredients = "viverra",
                    photo = "neglegentur",
                    isItCooked = false,
                    rating = 1.0f,
                    pasos = "null"
                ),
                RecipesModel(
                    id = 1,
                    category = listOf(),
                    name = "sopa de Caracoles",
                    ingredients = "non",
                    photo = "maecenas",
                    isItCooked = false,
                    rating = 5.0f,
                    pasos = "null"
                ),
                RecipesModel(
                    id = 3,
                    category = listOf(),
                    name = "Tarta de fresas",
                    ingredients = "viverra",
                    photo = "neglegentur",
                    isItCooked = false,
                    rating = 4.0f,
                    pasos = "null"
                ),
            )
            coEvery { searchRecipesUseCase.invoke() } returns recipesList
            viewModel = HomeViewModel(searchRecipesUseCase, saveRecipesUseCase, scrapNews)
            viewModel.getBestRatedRecipes()
            assertThat(viewModel.bestRatedRecipesList.value.size == 2).isTrue()
        }
}