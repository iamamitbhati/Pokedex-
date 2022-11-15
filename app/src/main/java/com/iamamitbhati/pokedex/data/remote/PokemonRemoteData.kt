package com.iamamitbhati.pokedex.data.remote

import com.iamamitbhati.pokedex.data.network.PokeService
import com.iamamitbhati.pokedex.model.PokemonResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * This class would handle all the remote api calls
 */
class PokemonRemoteData @Inject constructor(private val pokeService: PokeService) {
    companion object {
        private const val PAGING_SIZE = 20
    }

    suspend fun getAllPokemon(page: Int): Response<PokemonResponse> {
        return pokeService.getAllPokemon(
            limit = PAGING_SIZE,
            offset = page * PAGING_SIZE
        )
    }
}