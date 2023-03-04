package com.cumpatomas.brunosrecipes.ui.input

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ChipDefaults.filterChipColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.components.LoadingAnimation
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import kotlinx.coroutines.launch

class InputComposeFragment : Fragment() {

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            setContent {

                val viewModel = viewModel<InputComposeViewModel>()
                val possibleRecipeList = viewModel.posibleRecipesList
                val modalSheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
                    skipHalfExpanded = false,
                )

                ModalBottomSheetLayout(
                    sheetState = modalSheetState,
                    sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    sheetContent = {
                        PosibleRecipesListScreen(
                            possibleRecipeList,
                            modalSheetState
                        )
                    },
                    content = {

                        InputMainScreen(viewModel, modalSheetState)
                    }
                )
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    @OptIn(ExperimentalMaterialApi::class)
    private fun InputMainScreen(
        viewModel: InputComposeViewModel,
        modalSheetState: ModalBottomSheetState
    ) {

        val ingredientsList = viewModel.ingredientsList
        val firstLettersList = ingredientsList.value.map { it.first() }.distinct()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val coroutineScope = rememberCoroutineScope()
        val loadingState = viewModel.isLoadingState

        Scaffold(
            scaffoldState = scaffoldState,
        ) {


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(colorResource(id = R.color.superlightgray))
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(colorResource(id = R.color.primaryLightColor))
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "¿Qué cocino?",
                        fontFamily = FontFamily(Font(R.font.beautiful_people)),
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.white),
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )

                }

                Row(horizontalArrangement = Arrangement.Center) {
                    if (viewModel.posibleRecipesNumber.value == 1)
                        Text(
                            text = "Hay ${viewModel.posibleRecipesNumber.value} receta posible!!",
                            fontFamily = FontFamily(Font(R.font.marlin_sans)),
                            textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        )
                    else if ((viewModel.posibleRecipesNumber.value > 1))
                        Text(
                            text = "Hay ${viewModel.posibleRecipesNumber.value} recetas posibles!!",
                            fontFamily = FontFamily(Font(R.font.marlin_sans)),
                            textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        )
                    else {
                        // no text
                    }

                }


                if (viewModel.posibleRecipesNumber.value != 0) {

                    Button(
                        onClick = {
                            coroutineScope.launch() {
                                modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(
                                id = R.color.superlightgreenbutton
                            ),
                            contentColor = colorResource(id = R.color.white)
                        ),
                        enabled = viewModel.posibleRecipesNumber.value > 0
                    ) {
                        Text(
                            text = "Ver Recetas",
                            fontFamily = FontFamily(Font(R.font.marlin_sans)),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = stringResource(id = R.string.choose_ingredients),
                            fontFamily = FontFamily(Font(R.font.marlin_sans)),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        )
                    }
                }
                if (loadingState.value) {
                    LoadingAnimation()
                }
                IngredientsRecyclerView(
                    firstLettersList,
                    ingredientsList.value,
                    viewModel,
                    loadingState
                )
            }
        }
    }

    @Composable
    private fun IngredientsRecyclerView(
        firstLettersList: List<Char>,
        ingredientsList: List<String>,
        viewModel: InputComposeViewModel,
        loadingState: MutableState<Boolean>,
    ) {

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {


            itemsIndexed(items = firstLettersList) { index, letter ->

                if (index % 2 == 0)
                    LetterAndChipsRow(
                        letter,
                        ingredientsList,
                        R.color.primaryLightColor2,
                    )
                else
                    LetterAndChipsRow(
                        letter,
                        ingredientsList,
                        R.color.secondaryDarkColor2,
                    )
                loadingState.value = false
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        resetIngredients(viewModel)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(
                            id = R.color.superlightred
                        ),
                        contentColor = colorResource(id = R.color.white)
                    ),
                    enabled = !viewModel.selectedIngredientsListState.value

                ) {

                    Text(
                        text = "Borrar Ingredientes",
                        fontStyle = FontStyle.Normal,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    private fun resetIngredients(viewModel: InputComposeViewModel) {
        viewModel.selectedIngredientsList.clear()
        viewModel.posibleRecipesList.value = emptyList()
        viewModel.posibleRecipesNumber.value = 0
        viewModel.selectedIngredientsListState.value = true
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun PosibleRecipesListScreen(
        possibleRecipeList: MutableState<List<RecipesModel>>,
        modalSheetState: ModalBottomSheetState
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxHeight(0.93f),
            backgroundColor = colorResource(id = R.color.white),
            floatingActionButton = {
/*                BackFloatingActionButton(
                    modalSheetState
                )*/
            },
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                item {
                    Text(
                        text = "Puedes cocinar...",
                        modifier = Modifier.padding(8.dp),
                        fontFamily = FontFamily(Font(R.font.marlin_sans)),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(possibleRecipeList.value) { recipe ->
                    Column() {
                        Card(
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                                .clickable {
                                    val action =
                                        InputComposeFragmentDirections.actionInputComposeFragmentToRecipeFragment(
                                            recipe.id
                                        )
                                    findNavController().navigate(action)
                                },
                            shape = RoundedCornerShape(8.dp),
                            backgroundColor = colorResource(id = R.color.primaryColor)
                        ) {
                            Row(verticalAlignment = CenterVertically) {
                                Image(
                                    painter = rememberAsyncImagePainter(recipe.photo),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(50.dp)
                                        .clip(CircleShape)
                                )
                                Text(
                                    text = recipe.name,
                                    modifier = Modifier.padding(8.dp),
                                    fontFamily = FontFamily(Font(R.font.marlin_sans)),
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                }

            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun BackFloatingActionButton(
        modalSheetState: ModalBottomSheetState
    ) {
        val coroutineScope = rememberCoroutineScope()
        FloatingActionButton(onClick = {
            coroutineScope.launch {
                modalSheetState.hide()
            }
        })
        {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            
            )
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun LetterAndChipsRow(
        letter: Char,
        ingredientsList: List<String>,
        color: Int,
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(initialAlpha = 0.3f),
            exit = fadeOut(),
        ) {
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier
                    .animateEnterExit(
                        // Slide in/out the inner box.
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally()
                    )
                    .padding()
                    .background(
                        color = colorResource(id = color),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = " ${letter.uppercase()} :  ",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
                ChipRow(ingredientsList.filter { it.first() == letter })
            }
        }

    }

    @Composable
    private fun ChipRow(ingredientsList: List<String>) {

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(ingredientsList) { ingredient ->
                IngredientChip(ingredient)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun IngredientChip(
        ingredient: String,
        viewModel: InputComposeViewModel = viewModel()
    ) {

        val isSelected =
            if (viewModel.selectedIngredientsListState.value) remember { mutableStateOf(false) }
            else if (ingredient in viewModel.selectedIngredientsList) remember { mutableStateOf(true) }
            else remember { mutableStateOf(false) }

        FilterChip(
            selected = isSelected.value,
            onClick = {
                isSelected.value = !isSelected.value
                viewModel.selectedIngredients(ingredient)
            },
            modifier = Modifier.padding(0.dp),
            shape = RoundedCornerShape(8.dp),
            colors = filterChipColors(
                backgroundColor = Color.Transparent,
                selectedBackgroundColor = Color.Transparent
            )
        ) {
            Text(
                text = ingredient,
                fontFamily = FontFamily(Font(R.font.marlin_sans)),
                fontWeight = if (isSelected.value) FontWeight.Bold else FontWeight.Normal,
                textDecoration = if (isSelected.value) TextDecoration.Underline else TextDecoration.None,
                fontSize = if (isSelected.value) 16.sp else 15.sp,
                color = if (isSelected.value) Color.Black else Color.DarkGray
            )
        }
    }

}
