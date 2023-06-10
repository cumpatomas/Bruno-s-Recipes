package com.cumpatomas.brunosrecipes.di

import androidx.room.Room
import com.cumpatomas.brunosrecipes.data.localdb.LocalDatabase
import com.cumpatomas.brunosrecipes.data.localdb.NewsDao
import com.cumpatomas.brunosrecipes.data.localdb.RecipesDao
import com.cumpatomas.brunosrecipes.manualdi.ApplicationModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDatabaseModule {

  /*  val db: LocalDatabase by lazy {
        provideDatabase()
    }*/

    @Provides
    @Singleton
    fun provideDatabase(): LocalDatabase {
        return Room.databaseBuilder(
            ApplicationModule.applicationContext,
            LocalDatabase::class.java,
            "local_db"
        ).build()
    }
    @Provides
    fun provideRecipesDao(db: LocalDatabase) : RecipesDao {
        return db.getRecipesDao()
    }

    @Provides
    fun provideNewsDao(db: LocalDatabase) : NewsDao {
        return db.getNewsDao()
    }
}