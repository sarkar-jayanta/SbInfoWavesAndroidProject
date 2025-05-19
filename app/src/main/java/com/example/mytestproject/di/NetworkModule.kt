package com.example.mytestproject.di

import com.android_boilerplate.BuildConfig
import com.example.mytestproject.ApiInterface
import com.example.mytestproject.AppConstants

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

@Singleton
@Provides
    fun providesBaseUrl() : String {
        return AppConstants.BASE_URL
    }

    @Singleton
    @Provides
    fun providesApiInterface( retrofit : Retrofit) : ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideLogging() : Boolean =
        BuildConfig.DEBUG

}