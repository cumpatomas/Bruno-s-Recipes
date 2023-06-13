package com.cumpatomas.brunosrecipes.data.localdb

import android.content.Context
import com.cumpatomas.brunosrecipes.manualdi.ApplicationModule
import javax.inject.Inject

class Preferences @Inject constructor() {

    private val sharedPreferences = ApplicationModule.applicationContext.getSharedPreferences("local_preferences", Context.MODE_PRIVATE)

    fun setIsNotVirgin() {
        sharedPreferences.edit().putBoolean("is_virgin", false).apply()
    }

    fun isVirgin() = sharedPreferences.getBoolean("is_virgin", true)
}