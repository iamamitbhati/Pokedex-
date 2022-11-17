package com.iamamitbhati.pokedex.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.repository.PokemonRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var favoriteViewModel: FavoriteViewModel

    @MockK
    private lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(Dispatchers.Unconfined)
        favoriteViewModel = FavoriteViewModel(pokemonRepository)
    }

    @Test
    fun `get all favorite pokemons valid list`() {
        val mockPokemonList = getMockList()
        val size = mockPokemonList.size

        coEvery { pokemonRepository.getAllFavorite() } returns flow {
            emit(mockPokemonList)
        }

        favoriteViewModel.getFavPokemons()
        val pokemons = favoriteViewModel.stateChange.getOrAwaitValue()
        assertEquals(pokemons.size, size)
        assertEquals(pokemons, mockPokemonList)
        assertTrue(pokemons[0].isFav)
        assertTrue(pokemons[1].isFav)
        assertTrue(pokemons[2].isFav)
    }


    @Test
    fun `get all favorite pokemons success empty list`()  {
        val emptyExchangeRate = emptyList<Pokemon>()
        coEvery { pokemonRepository.getAllFavorite() } returns flow {
            emit(emptyExchangeRate)
        }

        favoriteViewModel.getFavPokemons()
        val pokemons = favoriteViewModel.stateChange.getOrAwaitValue()
        assertEquals(pokemons.size, 0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getMockList() = mutableListOf<Pokemon>().also {
        it.add(Pokemon(1, "bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/", true))
        it.add(Pokemon(2, "ivysaur", "https://pokeapi.co/api/v2/pokemon/2/", true))
        it.add(Pokemon(2, "venusaur", "https://pokeapi.co/api/v2/pokemon/2/", true))
    }
}