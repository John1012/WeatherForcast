package com.assignment.john.repository.di

import com.assignment.john.repository.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideWeatherRepoistory(): WeatherRepository {
        return WeatherRepository()
    }
}