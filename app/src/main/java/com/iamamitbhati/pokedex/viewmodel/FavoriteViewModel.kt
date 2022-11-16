package com.iamamitbhati.pokedex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.model.PokemonDetailsEntity
import com.iamamitbhati.pokedex.repository.PokemonRepository
import com.iamamitbhati.pokedex.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val pokemonRepository: PokemonRepository) :
    ViewModel() {
    init {
        getFavPokemons()
    }

    val stateChange: MutableLiveData<List<Pokemon>> = MutableLiveData()
    fun getFavPokemons() {
        viewModelScope.launch {
            pokemonRepository.getAllFavorite().collect {
                stateChange.postValue(it)
            }
        }
    }
}