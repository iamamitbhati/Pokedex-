package com.iamamitbhati.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iamamitbhati.pokedex.databinding.FragmentPokemonBinding

class FragmentPokemonList :Fragment() {
    lateinit var binding: FragmentPokemonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }
}