package com.iamamitbhati.pokedex.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType> {

    fun asFlow(page:Int) = flow<Resource<ResultType>>{
        emit(Resource.Loading())
        val list = getList(page)
        if (shouldFetch(list)) {
            //emit(Resource.Loading())
            val apiResponse = fetchFromNetwork()
            if (apiResponse.isSuccessful) {
                processResponse(apiResponse)?.let { saveNetworkResult(it,page) }
                emitAll(getAllList(page).map {
                    Resource.Success(it)
                })
            } else {
                emit(Resource.Failed(apiResponse.message()))
            }
        } else {
            emitAll(getAllList(page).map { Resource.Success(it) })
        }
    }
    @WorkerThread
    protected open fun processResponse(response: Response<RequestType>) = response.body()

    @WorkerThread
    protected abstract suspend fun saveNetworkResult(item: RequestType, page: Int)

    @MainThread
    protected abstract fun shouldFetch(resultType: ResultType): Boolean

    @MainThread
    protected abstract fun getList(page: Int): ResultType

    @MainThread
    protected abstract fun getAllList(page: Int): Flow<ResultType>

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Response<RequestType>
}