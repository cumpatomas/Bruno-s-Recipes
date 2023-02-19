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
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.components.LoadingAnimation
import com.cumpatomas.brunosrecipes.components.NoInternetMge
import com.cumpatomas.brunosrecipes.data.network.ConnectivityObserver
import com.cumpatomas.brunosrecipes.data.network.NetworkConnectivityObserver
import com.cumpatomas.brunosrecipes.domain.model.NewsModel
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.manualdi.ApplicationModule.applicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        view.findViewById<ComposeView>(R.id.composeView).setContent {
        }

        return view
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector", "CoroutineCreationDuringComposition")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeView).setContent {

            val internetStatus by connectivityObserver.observe().collectAsState(
                initial = ConnectivityObserver.Status.Unavailable
            )
            val viewModel = viewModel<HomeViewModel>()

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    connectivityObserver = NetworkConnectivityObserver(applicationContext)
                    viewModel.getRecipes()
                    viewModel.closeNewsCard()
                    viewModel.getNews()
                    viewModel.helpSurfaceState.value = false
                }
            }
            HomeScreen(viewModel, internetStatus)
        }
    }

    @Composable
    fun HomeScreen(
        viewModel: HomeViewModel,
        internetStatus: ConnectivityObserver.Status,
        modifier: Modifier = Modifier
    ) {
        val newsestRecipesList = viewModel.newestRecipesList
        val bestRecipesList = viewModel.bestRatedRecipesList

        if (viewModel.helpSurfaceState.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    TitleText()
                }
                Row() {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.95f)
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        HelpSurfaceItem(viewModel.helpSurfaceText ?: "")
                    }
                }

            }
        } else

            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = modifier
                    .padding(horizontal = 0.dp)
                    .verticalScroll(ScrollState(0))
            ) {
                if (internetStatus == ConnectivityObserver.Status.Lost) {
                    Surface(
                        color = colorResource(id = R.color.superlightred),
                        shape = RoundedCornerShape(12.dp),
                    ) {

                        Text(
                            text = "No hay conexión de red",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(8.dp),
                        )

                    }
                }
                TitleText()

                if (bestRecipesList.value.isNotEmpty()) {
                    HomeSection(title = "Tus recetas mejor valoradas") {
                        BestRatedRecipes(bestRecipesList)
                    }
                } else {
                    HomeSection(title = "Comencemos!") {
                        StartRow()
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                if (internetStatus == ConnectivityObserver.Status.Unavailable) {
                    if(newsestRecipesList.value.isEmpty()) {

                    }

                } else {
                    if (viewModel.isLoadingState.value) {
                        HomeSection(title = "Últimas recetas agregadas") {
                            LoadingAnimation(
                                circleColor = colorResource(id = R.color.white),
                                circleSize = 12.dp
                            )
                        }

                    } else {
                        HomeSection(title = "Últimas recetas agregadas") {
                            NewestRecipesRow(newsestRecipesList)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                HomeSection(title = "Noticias") {
                    NewsColumn(viewModel.newsList.value, internetStatus)
                }
            }
    }

    private @Composable
    fun HelpSurfaceItem(title: String) {
        when (title) {
            "Buscador de recetas" -> {

                Text(
                    text = title,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.beautiful_people)),
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp),
                )
            }
            "Marcar tus recetas" -> {

                Text(
                    text = title,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.beautiful_people)),
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp),
                )
            }
            "Puntúa tus recetas" -> {

                Text(
                    text = title,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.beautiful_people)),
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp),
                )
            }
            "¿Qué puedo cocinar?" -> {

                Text(
                    text = title,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.beautiful_people)),
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }


    private @Composable
    fun TitleText() {
        Text(
            text = "- Recetas de Bruno -",
            fontFamily = FontFamily(Font(R.font.beautiful_people)),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 38.sp,
            modifier = Modifier
                .paddingFromBaseline(top = 24.dp)
                .padding(horizontal = 8.dp, vertical = 16.dp)
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun NewsWebView(new: NewsModel) {
        val loadingState = remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()

        Surface(
            modifier = Modifier
                .height(450.dp)
                .verticalScroll(rememberScrollState())
        ) {

            AndroidView(
                factory = {
                    WebView(it).apply {
                        coroutineScope.launch {
                            val job = launch {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                loadUrl(new.link)

                            }
                            job.join()
                            delay(2000)
                            loadingState.value = false
                        }
                    }
                }, /*update = {
                it.loadUrl(new.link)
            }*/
            )

        }
        if (loadingState.value) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LoadingAnimation(
                    circleColor = colorResource(id = R.color.secondaryColor),
                    circleSize = 12.dp,
                    modifier = Modifier
                        .alpha(0.7f)
                        .padding(top = 24.dp)
                )
            }

        } else
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(8.dp)
            ) {
                CloseButton(new)
            }
    }

    @Composable
    fun CloseButton(
        new: NewsModel,
    ) {
        val coroutineScope = rememberCoroutineScope()
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    new.state.value = false
                }
            },
            modifier = Modifier
                .size(50.dp)
                .alpha(0.7f)
        )
        {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier.padding(0.dp)
            )
        }
    }

    @Composable
    fun NewsColumn(
        newsList: List<NewsModel>,
        internetStatus: ConnectivityObserver.Status,
        viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    ) {

        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        )

        {
            if (newsList.isNotEmpty()) {
                YukaCard()
                for (i in 0..9)
                    NewsItem(newsList[i])
            }
            if (internetStatus == ConnectivityObserver.Status.Unavailable
                || internetStatus == ConnectivityObserver.Status.Lost
                ) {
                if (newsList.isEmpty()) {
                    NoInternetMge()
                }
            } else{
                if (newsList.isNotEmpty()) {
                    for (i in 0..9)
                        NewsItem(newsList[i])
                }

            }
        }
    }

    @Composable
    private fun YukaCard() {
        val YukaWebViewState = remember { mutableStateOf(false) }

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp)
                .clickable {
                    YukaWebViewState.value = !YukaWebViewState.value
                }
        ) {
            if (YukaWebViewState.value)
                YucaWebView(YukaWebViewState)
            else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Conoce la app de",
                        fontFamily = FontFamily(Font(R.font.marlin_sans)),
                        color = colorResource(id = R.color.mid_gray),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .paddingFromBaseline(top = 28.dp)
                            .padding(horizontal = 8.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.yuka_logo),
                        contentDescription = "Yuka",
                    )
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    private fun YucaWebView(YukaWebViewState: MutableState<Boolean>) {
        val coroutineScope = rememberCoroutineScope()
        val loadingState = remember { mutableStateOf(true) }
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp)
                .height(450.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())

        ) {

            AndroidView(
                factory = {
                    WebView(it).apply {
                        coroutineScope.launch {
                            val job = launch {

                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                loadUrl("https://yuka.io/es/aplicacion/")
                            }
                            job.join()
                            delay(3000)
                            loadingState.value = false
                        }

                    }
                },
                update = {
                    it.loadUrl("https://yuka.io/es/aplicacion/")
                }

            )
        }
        if (loadingState.value) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LoadingAnimation(
                    circleColor = colorResource(id = R.color.secondaryColor),
                    circleSize = 12.dp,
                    modifier = Modifier
                        .alpha(0.7f)
                        .padding(top = 24.dp)
                )
            }

        } else {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(8.dp)
            ) {

                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            YukaWebViewState.value = false
                        }
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .alpha(0.7f)
                )
                {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Volver",
                        tint = Color.White,
                        modifier = Modifier.padding(0.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun NewsItem(
        new: NewsModel,
    ) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp)
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
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .paddingFromBaseline(top = 24.dp)
                            .padding(horizontal = 8.dp)
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

    @Composable
    private fun StartRow() {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            StartRowElement("Buscador de recetas", R.drawable.recipe_list_image)
            StartRowElement("Marcar tus recetas", R.drawable.cooked_recipe_image)
            StartRowElement("Puntúa tus recetas", R.drawable.rate_recipe)
            StartRowElement("¿Qué puedo cocinar?", R.drawable.input_ingredients)
        }
    }

    @Composable
    private fun StartRowElement(
        text: String,
        @DrawableRes drawable: Int,
        viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 0.dp, bottom = 16.dp)
                .width(110.dp)
        ) {
            Surface(
                elevation = 14.dp,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.clickable {

                    viewModel.helpSurfaceText = text
                    viewModel.helpSurfaceState.value = true

                }
            ) {
                Image(
                    painter = painterResource(id = drawable),
                    contentDescription = null,
                    contentScale = Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

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
            items(bestRecipesList.value) { recipe ->
                BestRatedCard(
                    photo = recipe.photo,
                    text = recipe.name,
                    rate = recipe.rating,
                    id = recipe.id
                )
            }
        }
    }

    @Composable
    private fun BestRatedCard(
        photo: String,
        text: String,
        rate: Float,
        id: Int,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = Modifier
                .width(180.dp)
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp, top = 8.dp)
                .clickable {
                    val action = HomeFragmentDirections.actionHomeFragmentToRecipeFragment(id)
                    findNavController().navigate(action)
                },
            elevation = 12.dp,
            shape = RoundedCornerShape(16.dp)

        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.superlightgreen),
                        fontFamily = FontFamily(Font(R.font.marlin_sans)),
                        fontWeight = FontWeight.Bold,
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = colorResource(id = R.color.superlightgreen),
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = rememberAsyncImagePainter(photo),
                    contentDescription = null,
                    contentScale = Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = text,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.marlin_sans))
                )
            }

        }
    }

    @Composable
    private fun NewestRecipesRow(
        recipesList: State<List<RecipesModel>>,
        modifier: Modifier = Modifier
    ) {
        Surface(
            elevation = 14.dp,
        )
        {
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
                        text = recipe.name,
                        id = recipe.id

                    )
                }
            }
        }
    }

    @Composable
    private fun NewestRecipesElement(
        photo: String,
        text: String,
        id: Int,
        modifier: Modifier = Modifier
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            modifier = modifier
                .width(100.dp)
                .clickable {
                    val action = HomeFragmentDirections.actionHomeFragmentToRecipeFragment(id)
                    findNavController().navigate(action)
                },
        ) {

            Image(
                painter = rememberAsyncImagePainter(photo),
                contentDescription = null,
                contentScale = Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
            )
            Text(
                text = text,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.marlin_sans)),
                modifier = Modifier.paddingFromBaseline(top = 24.dp)
            )
        }
    }

    @Composable
    fun HomeSection(title: String, content: @Composable () -> Unit) {

        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.beautiful_people)),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}

