package com.cumpatomas.brunosrecipes.ui.recipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.brunosrecipes.domain.SaveRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val saveRecipesUseCase: SaveRecipesUseCase,
    private val searchRecipesUseCase: SearchRecipesUseCase,
) : ViewModel() {
    // Declaramos el Estado de la Vista para actualizar
    private val _viewState = Channel<RecipesListViewState>()
    val viewState = _viewState.receiveAsFlow()
    private val _recipesList = MutableStateFlow<List<RecipesModel>>(emptyList())
    val recipesList = _recipesList.asStateFlow()
    private val _categoryList = MutableStateFlow<List<String>>(emptyList())
    val categoryList = _categoryList.asStateFlow()
    var categoriesSelected = mutableListOf<String>()
        private set
    private var query = ""
    private var searchJob: Job? = null // Job es el launch o hilo lanzado de una corrutina
    var testList = emptyList<RecipesModel>()

    init {
        viewModelScope.launch(IO) {
            launch { _viewState.send(RecipesListViewState(loading = true)) }
            val saveRecipesJob = launch {
                saveRecipesUseCase.invoke()
            }
            saveRecipesJob.join()

            launch {
                searchRecipesUseCase.invoke().also { recipeList ->
                    _recipesList.value = recipeList

                    _categoryList.value = recipeList.flatMap { recipe ->
                        recipe.category
                    }.distinct()
                }
            }
            launch { _viewState.send(RecipesListViewState(loading = false)) }
        }
    }

    fun searchList(query: String = this.query) {

        this.query = query
        searchJob?.cancel()

        searchJob = viewModelScope.launch(IO) {
            launch { _viewState.send(RecipesListViewState(loading = true)) }
            val newList =
                if (categoriesSelected.isEmpty()) {
                    searchRecipesUseCase.invoke(this@RecipesListViewModel.query)
                } else {
                    searchRecipesUseCase.invoke(this@RecipesListViewModel.query).filter { recipe ->
                        recipe.category.containsAll(categoriesSelected)
                    }
                }
            updateRecipesList(newList)
            launch { _viewState.send(RecipesListViewState(loading = false)) }
        }
    }

    fun updateRecipesList(newList: List<RecipesModel>) {
        _recipesList.value = newList
        testList = newList

    }

    fun addCategoriesSelected(category: String) {
        categoriesSelected.add(category)
    }

    fun clearCategoriesSelected() = categoriesSelected.clear()
}