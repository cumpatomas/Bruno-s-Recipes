package com.cumpatomas.brunosrecipes.manualdi

import android.app.Application

object ApplicationModule {

    lateinit var applicationContext: Application

    fun initialiseApplicationContext(_application: Application) {
        this.applicationContext = _application
    }

    fun isInitialised() = ApplicationModule::applicationContext.isInitialized
}