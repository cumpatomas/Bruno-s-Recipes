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
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val saveRecipesUseCase: SaveRecipesUseCase
) : ViewModel() {

    val newestRecipesList = mutableStateOf<List<RecipesModel>>(emptyList())
    val bestRatedRecipesList = mutableStateOf<List<RecipesModel>>(emptyList())
    val isLoadingState: MutableState<Boolean> = mutableStateOf(true)
    val scrapNews = ScrapNews()
    val newsList = mutableStateOf<List<NewsModel>>(emptyList())
    val helpSurfaceState = mutableStateOf(false)
    var helpSurfaceText: String? = null

    init {
        viewModelScope.launch {

            isLoadingState.value = true
            val saveRecipesJob = launch {
                saveRecipesUseCase.invoke()

            }
            saveRecipesJob.join()
             val newsJob = launch(Default) {
                 newsList.value = scrapNews.invoke()
             }
             newsJob.join()
            getRecipes()
            isLoadingState.value = false
        }
    }
    suspend fun getNews() {
        viewModelScope.launch(Default) {
            newsList.value = scrapNews.invoke()
        }
    }

    suspend fun getRecipes() {

        searchRecipesUseCase.invoke().also { list ->
            newestRecipesList.value = list.sortedBy { it.id }.reversed()
            val tempList = list.filter { it.rating > 2.0f }
            bestRatedRecipesList.value = tempList.sortedBy { it.rating }.reversed()
        }
    }

    suspend fun saveRecipes() {
        viewModelScope.launch {
            isLoadingState.value = true
            val saveRecipesJob = launch {
                saveRecipesUseCase.invoke()
            }
            saveRecipesJob.join()
            getRecipes()
            isLoadingState.value = false
        }

    }

    fun closeNewsCard() {
        for (i in newsList.value)
            i.state.value = false
    }

}



