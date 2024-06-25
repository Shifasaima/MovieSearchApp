package com.example.myapplication.di

import com.example.myapplication.internal.MovieApiService
import com.example.myapplication.internal.MovieRepository
import com.example.myapplication.internal.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieApiService(): MovieApiService {
        return Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService:: class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: MovieApiService): MovieRepository
    {
        return MovieRepositoryImpl(apiService)
    }
}