package com.iamamitbhati.pokedex.di

import android.app.Application
import androidx.room.Room
import com.iamamitbhati.pokedex.data.local.PokedexDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application
    ): PokedexDatabase {
        return Room
            .databaseBuilder(application, PokedexDatabase::class.java, "Pokedex.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideExchangeRateDao(pokedexDatabase: PokedexDatabase) = pokedexDatabase.pokemonDao()

}