package com.cumpatomas.brunosrecipes.ui.input

import android.annotation.SuppressLint
import android.graphics.drawable.PaintDrawable
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.LottieCompositionResult.Loading.composition
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.components.LoadingAnimation
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import kotlinx.coroutines.delay
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

        val ingredientsList =
            viewModel.getIngredientsFlow.collectAsState(initial = emptyList())
        val firstLettersList = ingredientsList.value.map { it.first() }.distinct()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val coroutineScope = rememberCoroutineScope()
        val loadingState = viewModel.isLoadingState

        Scaffold(
            scaffoldState = scaffoldState,
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = if (viewModel.posibleRecipesNumber.value == 1) "Hay ${viewModel.posibleRecipesNumber.value} receta posible!!"
                        else if ((viewModel.posibleRecipesNumber.value > 1)) "Hay ${viewModel.posibleRecipesNumber.value} recetas posibles!!"
                        else "Aún no hay recetas posibles",
                        fontFamily = FontFamily(Font(R.font.marlin_sans)),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

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
                                id = R.color.superlightgreen
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
                }
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = stringResource(id = R.string.choose_ingredients),
                        fontFamily = FontFamily(Font(R.font.marlin_sans)),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    )
                }
                if (loadingState.value) {
                    LoadingAnimation()
                }
                IngredientsRecyclerView(
                    firstLettersList,
                    ingredientsList,
                    viewModel,
                    loadingState
                )
            }
        }
    }


    @Composable
    private fun IngredientsRecyclerView(
        firstLettersList: List<Char>,
        ingredientsList: State<List<String>>,
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
                        text = "Borrar Ingredientes Seleccionados",
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
        backgroundColor = Color.White,
        floatingActionButton = {
            BackFloatingActionButton(
                modalSheetState
            )
        },
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            item { 
                Text(text = "Puedes cocinar...",
                    modifier = Modifier.padding(8.dp),
                    fontFamily = FontFamily(Font(R.font.marlin_sans)),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                textDecoration = TextDecoration.Underline)
            }

            items(possibleRecipeList.value) { recipe ->
                Column() {
                    Card(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                            .clickable { },
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = colorResource(id = R.color.primaryLightColor2)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LetterAndChipsRow(
    letter: Char,
    ingredientsList: State<List<String>>,
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
            ChipRow(ingredientsList.value.filter { it.first() == letter })
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
    viewModel: InputComposeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
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

@Composable
fun DotsFlashing() {
    val dotSize = 24.dp // made it bigger for demo
    val delayUnit = 200 // you can change delay to change animation speed
    val minAlpha = 0.1f

    @Composable
    fun Dot(
        alpha: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .alpha(alpha)
            .background(
                color = colorResource(id = R.color.primaryLightColor2),
                shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateAlphaWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                minAlpha at delay with LinearEasing
                1f at delay + delayUnit with LinearEasing
                minAlpha at delay + delayUnit * 2
            }
        )
    )

    val alpha1 by animateAlphaWithDelay(0)
    val alpha2 by animateAlphaWithDelay(delayUnit)
    val alpha3 by animateAlphaWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val spaceSize = 2.dp

        Dot(alpha1)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha2)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha3)
    }
}


/*
@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 15.dp,
    circleColor: Color = colorResource(id = R.color.primaryColor),
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 10.dp
) {
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        circleValues.forEach { value ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        translationY = -value * distance
                    }
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    )
            )
        }
    }
}*/
