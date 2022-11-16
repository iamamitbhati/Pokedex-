package com.iamamitbhati.pokedex.repository

import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.model.PokemonDetailsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Single entry point for repository
 */
interface PokemonRepository {
    suspend fun getAllPokemons(page:Int): Flow<Resource<List<Pokemon>>>

    suspend fun getPokemonDetail(name: String): Flow<Resource<PokemonDetailsEntity>>
}