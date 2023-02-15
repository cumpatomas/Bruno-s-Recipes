package com.cumpatomas.brunosrecipes.ui.homefragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.components.LoadingAnimation
import com.cumpatomas.brunosrecipes.domain.model.NewsModel
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        view.findViewById<ComposeView>(R.id.composeView).setContent {
        }

        return view
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector", "CoroutineCreationDuringComposition")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeView).setContent {
            val viewModel = viewModel<HomeViewModel>()

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getRecipes()
                }
            }
            HomeScreen(viewModel)
        }

    }

    @Composable
    fun HomeScreen(
        viewModel: HomeViewModel,
        modifier: Modifier = Modifier
    ) {
        val newsestRecipesList = viewModel.newestRecipesList
        val bestRecipesList = viewModel.bestRatedRecipesList
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(horizontal = 0.dp)
                .verticalScroll(ScrollState(0))
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (bestRecipesList.value.isNotEmpty()) {
                HomeSection(title = "Tus recetas mejor valoradas") {
                    BestRatedRecipes(bestRecipesList)
                }
            } else {
                HomeSection(title = "Empecemos!") {
                    StartRow()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (viewModel.isLoadingState.value) {
                LoadingAnimation(circleColor = colorResource(id = R.color.white))
            } else {
                HomeSection(title = "Últimas recetas agregadas") {
                    NewestRecipesRow(newsestRecipesList)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HomeSection(title = "Noticias Relacionadas") {
                NewsColumn(viewModel.newsList.value)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun NewsWebView(new: NewsModel) {

        // Declare a string that contains a url

        // Adding a WebView inside AndroidView
        // with layout as full screen
        Surface(modifier = Modifier
            .height(500.dp)
            .verticalScroll(rememberScrollState())) {
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(new.link)

                }
            }, update = {
                it.loadUrl(new.link)

            })


        }
        Row(horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(8.dp)) {
            CloseButton(new)
        }
    }
    @Composable
    fun CloseButton(
        new: NewsModel,
    ) {
        val coroutineScope = rememberCoroutineScope()
        FloatingActionButton(onClick = {
            coroutineScope.launch {
                new.state.value = false
            }
        })
        {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Volver",
                tint = Color.White,
            )
        }
    }

    @Composable
    fun NewsColumn(newsList: List<NewsModel>) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )

        {
            if (newsList.isNotEmpty())
                for (i in 0..9)
                    NewsItem(newsList[i])
            else {
                LoadingAnimation(circleColor = colorResource(id = R.color.white))
            }

        }
    }

    private @Composable
    fun NewsItem(new: NewsModel,
                 viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = new.title,
                        fontFamily = FontFamily(Font(R.font.marlin_sans)),
                        color = colorResource(id = R.color.mid_gray),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.paddingFromBaseline(top = 24.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "leer...",
                        fontFamily = FontFamily(Font(R.font.marlin_sans)),
                        color = colorResource(id = R.color.secondaryColor),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .paddingFromBaseline(bottom = 8.dp)
                            .padding(horizontal = 8.dp)
                            .clickable {

                                new.state.value = true
                            }
                    )

                }
            }

                if (new.state.value)
                    NewsWebView(new)
        }

    }

    private @Composable
    fun StartRow() {
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            StartRowElement("Buscador de recetas", R.drawable.recipe_list_image)
            StartRowElement("Marcar tus recetas", R.drawable.cooked_recipe_image)
            StartRowElement("Puntúa tus recetas", R.drawable.rate_recipe)
            StartRowElement("¿Qué puedo cocinar?", R.drawable.input_ingredients)
        }
    }


    private @Composable
    fun StartRowElement(
        text: String,
        @DrawableRes drawable: Int,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .width(120.dp)
        ) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = null,
                contentScale = Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Text(
                text = text,
                fontFamily = FontFamily(Font(R.font.marlin_sans)),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                modifier = Modifier.paddingFromBaseline(20.dp)
            )
        }
    }

    @Composable
    fun BestRatedRecipes(
        bestRecipesList: State<List<RecipesModel>>,
        modifier: Modifier = Modifier,
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(0.dp),
            modifier = modifier
        ) {
            items(bestRecipesList.value) { item ->
                BestRatedCard(
                    photo = item.photo,
                    text = item.name,
                    rate = item.rating
                )
            }
        }
    }

    private @Composable
    fun BestRatedCard(photo: String, text: String, rate: Float, modifier: Modifier = Modifier) {
        Card(
            modifier = Modifier
                .width(180.dp)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            elevation = 12.dp,
            shape = RoundedCornerShape(16.dp)


        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = when (rate) {
                        1.0f -> "1/5"
                        2.0f ->
                            "2/5"
                        3.0f ->
                            "3/5"
                        4.0f ->
                            "4/5"
                        else ->
                            "5/5"
                    },
                    fontSize = 22.sp,
                    color = colorResource(id = R.color.superlightgreen),
                    fontFamily = FontFamily(Font(R.font.marlin_sans)),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = rememberAsyncImagePainter(photo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = text,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.marlin_sans))
                    //style = MaterialTheme.typography.body2,
//                modifier = Modifier.paddingFromBaseline(top = 24.dp)
                )
            }

        }
    }

    private @Composable
    fun NewestRecipesRow(recipesList: State<List<RecipesModel>>, modifier: Modifier = Modifier) {
        LazyRow(
            modifier = modifier
                .background(color = Color.White)
                .padding(vertical = 16.dp)
                .height(140.dp),

            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(recipesList.value) { recipe ->
                NewestRecipesElement(
                    photo = recipe.photo,
                    text = recipe.name
                )
            }
        }
    }

    private @Composable
    fun NewestRecipesElement(
        photo: String,
        text: String,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier.width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(photo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
            )
            Text(
                text = text,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                //style = MaterialTheme.typography.h6,
                fontFamily = FontFamily(Font(R.font.marlin_sans)),
                modifier = Modifier.paddingFromBaseline(top = 24.dp)
            )
        }
    }

    @Composable
    fun HomeSection(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {

        Text(
            text = title.uppercase(),
            fontFamily = FontFamily(Font(R.font.marlin_sans)),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            //fontWeight = FontWeight.Bold,
            //fontStyle = FontStyle.Italic,

        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

