package com.iamamitbhati.pokedex.di

import com.iamamitbhati.pokedex.repository.PokemonRepository
import com.iamamitbhati.pokedex.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun providesRepositoryModule(pokemonRepositoryImpl : PokemonRepositoryImpl) : PokemonRepository
}