package com.cumpatomas.brunosrecipes.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.brunosrecipes.domain.GetTimesRecipesHaveBeenCooked
import com.cumpatomas.brunosrecipes.domain.DeleteRecipeFromHistory
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val deleteRecipeFromHistory: DeleteRecipeFromHistory) : ViewModel() {

    private val getTimesRecipesHaveBeenCooked = GetTimesRecipesHaveBeenCooked()
//    private val deleteRecipeFromHistory = DeleteRecipeFromHistory()

    private val _recipesCooked = MutableStateFlow<List<RecipesModel>>(emptyList())
    val recipesCooked = _recipesCooked.asStateFlow()

    init {
        viewModelScope.launch {
            getTimesRecipesHaveBeenCooked.invoke().collectLatest { recipesCooked ->
                _recipesCooked.value = recipesCooked
            }
        }
    }

    fun deleteRecipeFromList(id: Int, date: String) {
        viewModelScope.launch {
            deleteRecipeFromHistory.invoke(id, date)
        }
    }
}

