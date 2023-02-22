package com.cumpatomas.brunosrecipes.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cumpatomas.brunosrecipes.data.localdb.entities.NewsEntity
import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity


@Dao
interface NewsDao {


    @Insert
    suspend fun insertNews(newsList: List<NewsEntity>)

    @Query("SELECT * FROM news_entity")
    suspend fun getNewsList(): List<NewsEntity>

    @Query("UPDATE news_entity SET title = :updatedTitle, link = :updatedLink WHERE id = :id")
    suspend fun updateNews(id: Int, updatedTitle: String, updatedLink: String)
}