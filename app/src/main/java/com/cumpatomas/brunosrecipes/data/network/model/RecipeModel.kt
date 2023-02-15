package com.cumpatomas.brunosrecipes.data.network.model

data class RecipeModel(
    val id: String = "",
    val category: List<String> = emptyList(),
    val name: String? = null,
    val ingredients: String? = null,
    val photo: String? = null,
    val isItCooked: Boolean = false,
    val dateCooked: String? = null,
    val rating: Float = 0f,
    val pasos: String
)