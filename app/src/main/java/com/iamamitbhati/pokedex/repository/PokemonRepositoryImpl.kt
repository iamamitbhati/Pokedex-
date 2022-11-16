package com.iamamitbhati.pokedex.repository

import com.iamamitbhati.pokedex.data.domain.toDomain
import com.iamamitbhati.pokedex.data.domain.toEntity
import com.iamamitbhati.pokedex.data.local.dao.PokemonDao
import com.iamamitbhati.pokedex.data.local.dao.PokemonDetailDao
import com.iamamitbhati.pokedex.data.remote.PokemonRemoteData
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.model.PokemonDetailsEntity
import com.iamamitbhati.pokedex.model.PokemonResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonRemoteData: PokemonRemoteData,
    private val pokemonDao: PokemonDao,
    private val pokemonDetailDao: PokemonDetailDao,
    private val ioDispatcher: CoroutineContext
) : PokemonRepository {

    override suspend fun getAllPokemons(page: Int): Flow<Resource<List<Pokemon>>> {

        return object : NetworkBoundResource<List<Pokemon>, PokemonResponse>() {
            override suspend fun fetchFromNetwork(): Response<PokemonResponse> {
                return pokemonRemoteData.getAllPokemon(page)
            }

            override suspend fun saveNetworkResult(item: PokemonResponse, page: Int) {
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

    override suspend fun getPokemonDetail(name: String): Flow<Resource<PokemonDetailsEntity>> {
        return object :
            NetworkBoundResourcePokemonDetail<PokemonDetailsEntity, PokemonDetailsEntity>() {
            override suspend fun fetchFromNetwork(): Response<PokemonDetailsEntity> {
                return pokemonRemoteData.getPokemonDetails(name)
            }

            override suspend fun saveNetworkResult(item: PokemonDetailsEntity) {
                pokemonDetailDao.insertPokemonDetails(item)
            }

            override fun shouldFetch(resultType: PokemonDetailsEntity?): Boolean {
                return resultType == null
            }

            override fun getPokemon(name: String): PokemonDetailsEntity? {
                return pokemonDetailDao.getPokemonDetails(name)
            }
        }.asFlow(name).flowOn(ioDispatcher)
    }

    override suspend fun setFavoriteUnFavorite(name: String, isFav: Boolean) {
        CoroutineScope(ioDispatcher).launch {
            pokemonDao.setFavoriteUnFavorite(name, isFav)
        }

    }

    override suspend fun getAllFavorite(): Flow<List<Pokemon>> {
        return withContext(ioDispatcher) {
            flowOf(pokemonDao.getAllFavoritePokemonList().toDomain())
        }

    }
}