package com.iamamitbhati.pokedex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.model.PokemonResponse
import com.iamamitbhati.pokedex.repository.PokemonRepository
import com.iamamitbhati.pokedex.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val currencyRepository: PokemonRepository) :ViewModel() {
    val stateChange : MutableLiveData<Resource<List<Pokemon>>> = MutableLiveData()
    fun getPockemon() {
        viewModelScope.launch {
            currencyRepository.getAllPokemons(page = 0).collect {
                //TODO: Will add logic here to load and show data to UI
                stateChange.postValue(it)
            }
        }
    }
}