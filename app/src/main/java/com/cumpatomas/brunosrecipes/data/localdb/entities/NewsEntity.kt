package com.cumpatomas.brunosrecipes.data.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cumpatomas.brunosrecipes.domain.model.NewsModel

@Entity(tableName = "news_entity")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val link: String,
) {

}

fun NewsModel.toEntity() = NewsEntity(
    title = title,
    link = link,
)