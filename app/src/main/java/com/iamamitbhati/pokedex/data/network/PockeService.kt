package com.iamamitbhati.pokedex.data.network

import com.iamamitbhati.pokedex.model.PokemonDetailsEntity
import com.iamamitbhati.pokedex.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {
    @GET("pokemon")
    suspend fun getAllPokemon(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetail(@Path("name") name: String): Response<PokemonDetailsEntity>
}