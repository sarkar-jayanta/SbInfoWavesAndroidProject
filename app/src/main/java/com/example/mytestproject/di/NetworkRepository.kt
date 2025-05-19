package com.example.mytestproject.di

import com.example.mytestproject.repository.RemoteRepository
import com.example.mytestproject.repository.RemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkRepository {
    @Binds
    @Singleton
     abstract fun provideRemoteRepository (remoteRepository: RemoteRepositoryImpl) : RemoteRepository
}