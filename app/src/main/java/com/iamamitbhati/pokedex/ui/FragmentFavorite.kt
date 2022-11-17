package com.iamamitbhati.pokedex.ui

import android.R
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iamamitbhati.pokedex.databinding.FragmentPokemonBinding
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.ui.adapter.PokemonAdapter
import com.iamamitbhati.pokedex.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentFavorite : Fragment() {
    private lateinit var binding: FragmentPokemonBinding
    private lateinit var pokemonAdapter: PokemonAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var pokemons = ArrayList<Pokemon>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        with(binding) {
            pokemonAdapter = PokemonAdapter(pokemons) { pokemon ->
                val directions = FragmentPokemonListDirections.navigateToPokemonDetail(
                    pokemon.name,
                    pokemon.getImageUrl()
                )
                findNavController().navigate(directions)
            }
            recyclerView.adapter = pokemonAdapter

        }
    }

    private fun setupObserver() {
        favoriteViewModel.getFavPokemons()
        favoriteViewModel.stateChange.observe(viewLifecycleOwner) {
            binding.progressbar.visibility = View.GONE
            it?.let { list ->
                pokemons.addAll(list)

            }
            pokemonAdapter.notifyItemRangeChanged(0,it.size)

        }
    }
}