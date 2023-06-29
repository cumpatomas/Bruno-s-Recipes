package com.cumpatomas.brunosrecipes.data.localdb

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Preferences @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences = context.getSharedPreferences("local_preferences", Context.MODE_PRIVATE)

    fun setIsNotVirgin() {
        sharedPreferences.edit().putBoolean("is_virgin", false).apply()
    }

    fun isVirgin() = sharedPreferences.getBoolean("is_virgin", true)
}