package com.cumpatomas.brunosrecipes.di

import android.content.Context
import androidx.room.Room
import com.cumpatomas.brunosrecipes.data.localdb.LocalDatabase
import com.cumpatomas.brunosrecipes.data.localdb.NewsDao
import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import com.cumpatomas.brunosrecipes.manualdi.ApplicationModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDatabaseModule {
    /*  val db: LocalDatabase by lazy {
          provideDatabase()
      }*/
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            "local_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideRecipesDao(db: LocalDatabase): RecipesDao {
        return db.getRecipesDao()
    }

    @Singleton
    @Provides
    fun provideNewsDao(db: LocalDatabase): NewsDao {
        return db.getNewsDao()
    }
}