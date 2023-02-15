package com.cumpatomas.brunosrecipes.manualdi

import androidx.room.Room
import com.cumpatomas.brunosrecipes.data.localdb.LocalDatabase

object LocalDatabaseModule {

    val db: LocalDatabase by lazy {
        provideDatabase()
    }

    private fun provideDatabase(): LocalDatabase {
        return Room.databaseBuilder(ApplicationModule.applicationContext, LocalDatabase::class.java, "local_db").build()
    }
}