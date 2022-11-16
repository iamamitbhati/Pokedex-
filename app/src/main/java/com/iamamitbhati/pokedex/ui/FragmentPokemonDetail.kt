package com.iamamitbhati.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.iamamitbhati.pokedex.R
import com.iamamitbhati.pokedex.databinding.FragmentPokemonDetailBinding
import com.iamamitbhati.pokedex.model.PokemonDetailsEntity
import com.iamamitbhati.pokedex.repository.Resource
import com.iamamitbhati.pokedex.viewmodel.DetailViewModel
import com.iamamitbhati.pokedex.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentPokemonDetail : Fragment() {
    private lateinit var binding: FragmentPokemonDetailBinding
    private val args by navArgs<FragmentPokemonDetailArgs>()
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var pokemonName: String
    private lateinit var pokemonImage: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pokemonName = args.pokemonName
        pokemonImage = args.pokemonImage
        detailViewModel.getPokemonDetail(pokemonName)
        setupObserver()
    }

    private fun setupObserver() {
        detailViewModel.stateChange.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    it.data?.let { pokemon ->
                        setValues(pokemon)
                    }
                }
                is Resource.Failed -> {
                    onRetrieveError(it.errorMessage)
                }
            }
        }

    }

    private fun setValues(pokemonEntity: PokemonDetailsEntity) {
        with(binding) {
            pokemonEntity.apply {
                pokemonName.text = name
                Glide.with(root.context)
                    .load(pokemonImage)
                    .into(image)
                heightValue.text = String.format("%.1f M", height.toFloat() / 10)
                weightValue.text = String.format("%.1f KG", weight.toFloat() / 10)
                baseExperienceValue.text = experience.toString()
            }
        }
    }

    private fun onRetrieveError(errorMessage: String?) {
        val showMessage = errorMessage ?: getString(R.string.post_error)
        val errorSnackbar =
            Snackbar.make(binding.root, showMessage, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction(R.string.retry, errorClickListener)
        errorSnackbar.show()
    }

    private val errorClickListener = View.OnClickListener { loadMore(pokemonName) }

    private fun loadMore(name: String) {
        detailViewModel.getPokemonDetail(name)
    }
}