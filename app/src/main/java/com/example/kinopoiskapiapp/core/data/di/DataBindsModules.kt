package com.example.kinopoiskapiapp.core.data.di

import com.example.kinopoiskapiapp.core.data.repository.SearchRepository
import com.example.kinopoiskapiapp.core.data.service.SearchService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataBindsModules {

    @Binds
    @Singleton
    fun bindsSearchService(impl: SearchService.Impl): SearchService

    @Binds
    @Singleton
    fun bindsSearchRepository(impl: SearchRepository.Impl): SearchRepository

}