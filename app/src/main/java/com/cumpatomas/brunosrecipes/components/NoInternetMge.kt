package com.cumpatomas.brunosrecipes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cumpatomas.brunosrecipes.R

@Composable
fun NoInternetMge() {
    Image(painter = painterResource(id = R.drawable.no_connection),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(60.dp))
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Ups..no hay conexión de red",
        fontFamily = FontFamily(Font(R.font.marlin_sans)),
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 17.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    LoadingAnimation(
        circleColor = colorResource(id = R.color.white),
        circleSize = 12.dp
    )
    Spacer(modifier = Modifier.height(16.dp))
}