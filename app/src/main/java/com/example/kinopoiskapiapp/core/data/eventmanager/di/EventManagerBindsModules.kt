package com.example.kinopoiskapiapp.core.data.eventmanager.di

import com.example.kinopoiskapiapp.core.data.eventmanager.FetchMovieByIdEventManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface EventManagerBindsModules {

    @Binds
    @Singleton
    fun bindsFetchMovieByIdEventManager(impl: FetchMovieByIdEventManager.Impl): FetchMovieByIdEventManager
}