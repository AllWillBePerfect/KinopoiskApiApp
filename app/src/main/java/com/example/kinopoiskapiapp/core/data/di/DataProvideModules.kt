package com.example.kinopoiskapiapp.core.data.di

import android.content.Context
import com.example.kinopoiskapiapp.core.database.AppDatabase
import com.example.kinopoiskapiapp.core.network.ApiFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataProvideModules {

    @Singleton
    @Provides
    fun provideApiManager() = ApiFactory.getKonopoiskApi()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getDatabase(context).movieDao()
}