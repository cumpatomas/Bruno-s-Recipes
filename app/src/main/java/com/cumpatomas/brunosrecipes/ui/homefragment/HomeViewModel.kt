package com.cumpatomas.brunosrecipes.ui.homefragment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.brunosrecipes.domain.SaveRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.ScrapNews
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.NewsModel
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val saveRecipesUseCase: SaveRecipesUseCase,
    private val scrapNews: ScrapNews,
) : ViewModel() {
    val newestRecipesList = mutableStateOf<List<RecipesModel>>(emptyList())
    val bestRatedRecipesList = mutableStateOf<List<RecipesModel>>(emptyList())
    val isLoadingState: MutableState<Boolean> = mutableStateOf(true)
    val newsList = mutableStateOf<List<NewsModel>>(emptyList())
    val helpSurfaceState = mutableStateOf(false)
    var helpSurfaceText: String? = null

    init {
        isLoadingState.value = true
        viewModelScope.launch(IO) {
            val saveRecipesJob = launch(IO) {
                saveRecipesUseCase.invoke()
            }
            saveRecipesJob.join()
            val newsJob = launch(IO) {
                newsList.value = scrapNews.invoke()
            }
            newsJob.join()
            getBestRatedRecipes()
            isLoadingState.value = false
        }
    }

    suspend fun getNews() {
        viewModelScope.launch(IO) {
            newsList.value = scrapNews.invoke()
        }
    }

    suspend fun getBestRatedRecipes() {
        searchRecipesUseCase.invoke().also { list ->
            newestRecipesList.value = list.sortedBy { it.id }.reversed()
            val tempList = list.filter { it.rating > 2.0f }
            bestRatedRecipesList.value = tempList.sortedBy { it.rating }.reversed()
        }
    }

    fun closeNewsCard() {
        for (i in newsList.value)
            i.state.value = false
    }
}



