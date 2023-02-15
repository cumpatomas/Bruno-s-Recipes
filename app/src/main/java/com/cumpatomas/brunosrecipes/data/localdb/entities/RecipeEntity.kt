package com.cumpatomas.brunosrecipes.data.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cumpatomas.brunosrecipes.data.network.model.RecipeModel

@Entity(tableName = "recipes_entity")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val category: List<String>,
    val name: String,
    val ingredients: String,
    val photo: String,
    val isFavorite: Boolean = false,
    val isItCooked: Boolean = false,
    val datesCooked: MutableList<String> = mutableListOf(),
    var rating: Float,
    var pasos: String?
)

fun RecipeModel.toEntity() = RecipeEntity(
    category = category,
    name = name.orEmpty(),
    ingredients = ingredients.orEmpty(),
    photo = photo.orEmpty(),
    isItCooked = isItCooked,
    rating = rating,
    pasos = pasos


)