package com.cumpatomas.brunosrecipes.domain.model

import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity


data class RecipesModel(
    val id: Int,
    val category: List<String>,
    val name: String,
    val ingredients: String,
    val photo: String,
    var isItCooked: Boolean = false,
    var datesCooked: List<String> = emptyList(),
    var rating: Float,
    var pasos: String?
)

fun RecipeEntity.toDomain() = RecipesModel(
    id = id ?: 0,
    category = category,
    name = name,
    ingredients = ingredients,
    photo = photo,
    isItCooked = isItCooked,
    datesCooked = datesCooked,
    rating = rating,
    pasos = pasos

)