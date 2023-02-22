package com.cumpatomas.brunosrecipes.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cumpatomas.brunosrecipes.data.localdb.entities.NewsEntity
import com.cumpatomas.brunosrecipes.data.localdb.entities.RecipeEntity

@Database(entities = [RecipeEntity::class, NewsEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getRecipesDao(): RecipesDao
    abstract fun getNewsDao(): NewsDao
}