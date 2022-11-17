package com.iamamitbhati.pokedex.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.repository.PokemonRepository
import com.iamamitbhati.pokedex.repository.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MainViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel

    @MockK
    private lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mainViewModel = MainViewModel(pokemonRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `get all pokemons failure`() {
        val page = 1
        val errorMessage = "Connection time out"
        coEvery { pokemonRepository.getAllPokemons(page) } returns flow {
            emit(
                Resource.Failed(
                    errorMessage
                )
            )
        }

        mainViewModel.getPokemons(page)
        val onError= mainViewModel.stateChange.getOrAwaitValue()

        TestCase.assertEquals(onError.errorMessage, errorMessage)
    }

    @Test
    fun `get all pokemons valid list`() {
        val page = 1
        val mockPokemonList = getMockList()
        val size = mockPokemonList.size
        coEvery { pokemonRepository.getAllPokemons(page) } returns flow {
            emit(
                Resource.Success(
                    mockPokemonList
                )
            )
        }
        mainViewModel.getPokemons(page)
        val pokemons = mainViewModel.stateChange.getOrAwaitValue()
        assertEquals(pokemons.data?.size, size)
        assertEquals(pokemons.data, mockPokemonList)
    }


    @Test
    fun `get all pokemons success empty list`() {
        val page = 1
        val emptyExchangeRate = emptyList<Pokemon>()
        coEvery { pokemonRepository.getAllPokemons(page) } returns flow {
            emit(
                Resource.Success(
                    emptyExchangeRate
                )
            )
        }
        mainViewModel.getPokemons(page = 1)
        val pokemons= mainViewModel.stateChange.getOrAwaitValue()
        TestCase.assertEquals(pokemons.data?.size, 0)
    }

    @Test
    fun `set Favorite UnFavorite test`() {
        val name = "bulbasaur"
        val isFav = true
        coEvery { pokemonRepository.setFavoriteUnFavorite(name, isFav) } returns Unit
        mainViewModel.setFavoriteUnFavorite(name, isFav)

        coVerify { pokemonRepository.setFavoriteUnFavorite(name, isFav) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getMockList() = mutableListOf<Pokemon>().also {
        it.add(Pokemon(1, "bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/", false))
        it.add(Pokemon(2, "ivysaur", "https://pokeapi.co/api/v2/pokemon/2/", true))
        it.add(Pokemon(2, "venusaur", "https://pokeapi.co/api/v2/pokemon/2/", false))
    }
}