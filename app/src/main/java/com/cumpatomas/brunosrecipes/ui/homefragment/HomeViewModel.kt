package com.cumpatomas.brunosrecipes.ui.homefragment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.brunosrecipes.domain.GetNews
import com.cumpatomas.brunosrecipes.domain.SaveRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.NewsModel
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val searchRecipesUseCase = SearchRecipesUseCase()
    val saveRecipesUseCase = SaveRecipesUseCase()

    val newestRecipesList = mutableStateOf<List<RecipesModel>>(emptyList())
    val bestRatedRecipesList = mutableStateOf<List<RecipesModel>>(emptyList())
    val isLoadingState: MutableState<Boolean> = mutableStateOf(true)
    val getNews = GetNews()
    val newsList = mutableStateOf<List<NewsModel>>(emptyList())

    init {
        viewModelScope.launch {

            isLoadingState.value = true
            val saveRecipesJob = launch {
                saveRecipesUseCase.invoke()
                isLoadingState.value = false
            }
            saveRecipesJob.join()
            val newsJob = launch(Default) {
                newsList.value = getNews.invoke()
            }
            newsJob.join()
        }
    }

    suspend fun getRecipes() {

        searchRecipesUseCase.invoke().also { list ->
            newestRecipesList.value = list.sortedBy { it.id }.reversed()
            val tempList = list.filter { it.rating != 0.0f }
            bestRatedRecipesList.value = tempList.sortedBy { it.rating }.reversed()
        }
    }

}



