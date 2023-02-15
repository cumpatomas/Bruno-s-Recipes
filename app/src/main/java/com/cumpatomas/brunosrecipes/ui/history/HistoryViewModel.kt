package com.cumpatomas.brunosrecipes.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.brunosrecipes.domain.GetTimesRecipesHaveBeenCooked
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val getTimesRecipesHaveBeenCooked = GetTimesRecipesHaveBeenCooked()

    private val _recipesCooked = MutableStateFlow<List<RecipesModel>>(emptyList())
    val recipesCooked = _recipesCooked.asStateFlow()

    init {
        viewModelScope.launch {
            getTimesRecipesHaveBeenCooked.invoke().collectLatest { recipesCooked ->
                _recipesCooked.value = recipesCooked
            }
        }
        checkOldRecipes()
    }

    private fun checkOldRecipes() {

    }


}

