package com.iamamitbhati.pokedex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.repository.PokemonRepository
import com.iamamitbhati.pokedex.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val pokemonRepository: PokemonRepository) :
    ViewModel() {
    val stateChange: MutableLiveData<Resource<List<Pokemon>>> = MutableLiveData()
    fun getPokemons(page: Int) {
        viewModelScope.launch {
            pokemonRepository.getAllPokemons(page = page).collect {
                stateChange.postValue(it)
            }
        }
    }

    fun setFavoriteUnFavorite(name: String, isFav: Boolean) {
        viewModelScope.launch {
            pokemonRepository.setFavoriteUnFavorite(name,isFav)
        }

    }
}