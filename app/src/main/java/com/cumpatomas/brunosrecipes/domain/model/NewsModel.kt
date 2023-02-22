package com.cumpatomas.brunosrecipes.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.cumpatomas.brunosrecipes.data.localdb.entities.NewsEntity
import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity

data class NewsModel(
    val id: Int? = null,
    val title: String,
    val link: String,
    var state: MutableState<Boolean> = mutableStateOf(false)
)

fun NewsEntity.toDomain() = NewsModel(
    id = id ?: 0,
    title = title,
    link = link,

)