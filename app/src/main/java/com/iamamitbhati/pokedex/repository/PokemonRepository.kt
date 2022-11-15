package com.iamamitbhati.pokedex.repository

import com.iamamitbhati.pokedex.model.Pokemon
import kotlinx.coroutines.flow.Flow

/**
 * Single entry point for repository
 */
interface PokemonRepository {
    suspend fun getAllPokemons(page:Int): Flow<Resource<List<Pokemon>>>
}