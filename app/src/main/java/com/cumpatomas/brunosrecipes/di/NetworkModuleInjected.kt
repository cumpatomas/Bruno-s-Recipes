package com.cumpatomas.brunosrecipes.di

import com.cumpatomas.brunosrecipes.data.network.RecipesAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object NetworkModuleInjected {

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): RecipesAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://recetas-de-bruno.web.app/")
            .build()
            .create(RecipesAPI::class.java)
    }

    @Provides
    fun providesHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

}