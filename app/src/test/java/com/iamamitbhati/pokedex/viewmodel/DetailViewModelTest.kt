package com.iamamitbhati.pokedex.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iamamitbhati.pokedex.model.PokemonDetailsEntity
import com.iamamitbhati.pokedex.repository.PokemonRepository
import com.iamamitbhati.pokedex.repository.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var detailViewModel: DetailViewModel

    @MockK
    private lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        detailViewModel = DetailViewModel(pokemonRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `get pokemon detail failure`() {
        val errorMessage = "Connection time out"
        val mockName = "bulbasaur"
        coEvery { pokemonRepository.getPokemonDetail(mockName) } returns flow {
            emit(
                Resource.Failed(
                    errorMessage
                )
            )
        }

        detailViewModel.getPokemonDetail(mockName)
        val onError = detailViewModel.stateChange.getOrAwaitValue()

        TestCase.assertEquals(onError?.errorMessage, errorMessage)
    }

    @Test
    fun `get pokemon detail success`() {
        val mockEntity = getMockEntity()
        val mockName = "bulbasaur"
        coEvery { pokemonRepository.getPokemonDetail(mockName) } returns flow {
            emit(
                Resource.Success(
                    mockEntity
                )
            )
        }
        detailViewModel.getPokemonDetail(mockName)
        val pokemonInfo = detailViewModel.stateChange.getOrAwaitValue()

        assertEquals(pokemonInfo.data?.name, mockEntity.name)
        assertEquals(pokemonInfo.data?.height, mockEntity.height)
        assertEquals(pokemonInfo.data?.weight, mockEntity.weight)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getMockEntity() = PokemonDetailsEntity(
        id = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        experience = 64
    )

}

