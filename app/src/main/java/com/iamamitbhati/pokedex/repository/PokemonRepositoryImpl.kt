package com.iamamitbhati.pokedex.repository

import com.iamamitbhati.pokedex.data.domain.toDomain
import com.iamamitbhati.pokedex.data.domain.toEntity
import com.iamamitbhati.pokedex.data.local.dao.PokemonDao
import com.iamamitbhati.pokedex.data.remote.PokemonRemoteData
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.model.PokemonResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PokemonRepositoryImpl @Inject constructor(
    private val currencyRemoteData: PokemonRemoteData,
    private val pokemonDao: PokemonDao,
    private val ioDispatcher : CoroutineContext
) : PokemonRepository {

    override suspend fun getAllPokemons(page:Int): Flow<Resource<List<Pokemon>>> {

        return object : NetworkBoundResource<List<Pokemon>, PokemonResponse>() {
            override suspend fun fetchFromNetwork(): Response<PokemonResponse> {
                return currencyRemoteData.getAllPokemon(page)
            }

            override suspend fun saveNetworkResult(item: PokemonResponse, page:Int) {
               val pokemons = item.results
                pokemons.forEach { pokemon -> pokemon.page = page }
                pokemonDao.insertPokemonList(pokemons.toEntity())
            }

            override fun shouldFetch(resultType: List<Pokemon>): Boolean {
                return resultType.isEmpty()
            }

            override fun getList(page: Int): List<Pokemon> {
                return pokemonDao.getPokemonList(page).toDomain()
            }

        }.asFlow(page).flowOn(ioDispatcher)
    }
}