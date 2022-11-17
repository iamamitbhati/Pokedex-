package com.iamamitbhati.pokedex.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*
import retrofit2.Response

abstract class NetworkBoundResourcePokemonDetail<ResultType, RequestType> {

    fun asFlow(name:String) = flow<Resource<ResultType>>{
        emit(Resource.Loading())
        val pokemon = getPokemon(name)
        if (shouldFetch(pokemon)) {
            //emit(Resource.Loading())
            val apiResponse = fetchFromNetwork()
            if (apiResponse.isSuccessful) {
                processResponse(apiResponse)?.let { saveNetworkResult(it) }
                getPokemon(name)?.let { pokemon ->
                    emitAll(flowOf(pokemon).map {
                        Resource.Success(it)
                    })
                }

            } else {
                emit(Resource.Failed(apiResponse.message()))
            }
        } else {
            getPokemon(name)?.let { pokemon ->
                emitAll(flowOf(pokemon).map {
                    Resource.Success(it)
                })
            }
        }
    }
    @WorkerThread
    protected open fun processResponse(response: Response<RequestType>) = response.body()

    @WorkerThread
    protected abstract suspend fun saveNetworkResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(resultType: ResultType?): Boolean

    @MainThread
    protected abstract fun getPokemon(name: String): ResultType?

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Response<RequestType>
}