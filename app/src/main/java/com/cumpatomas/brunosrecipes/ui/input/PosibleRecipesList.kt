package com.cumpatomas.brunosrecipes.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.navArgs
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase

class PosibleRecipesListFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val viewModel = viewModel<InputComposeViewModel>()
                val posibleRecipesList = viewModel.posibleRecipesList.value
                val testList = listOf<String>("uno", "dos", "tres", "cuatro")

                LazyColumn() {

                    item{
/*                        Text(text = "Ingredientes seleccionados: ")
                        Spacer(modifier = Modifier.height(16.dp))
                        for (i in posibleRecipesListID) {
//                            Text(text = "$i ")
                        }*/
                    }

                    itemsIndexed(items = posibleRecipesList) { index, recipe ->
                        Row() {
                            Text(text = posibleRecipesList[index].name)
                        }
                    }
                }
            }
        }
    }
}
