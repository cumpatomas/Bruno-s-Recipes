package com.cumpatomas.brunosrecipes.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class NewsModel(val title: String, val link: String, var state: MutableState<Boolean> = mutableStateOf(false))