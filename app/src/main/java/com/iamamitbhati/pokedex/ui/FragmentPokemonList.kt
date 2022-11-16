package com.iamamitbhati.pokedex.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iamamitbhati.pokedex.R
import com.iamamitbhati.pokedex.databinding.FragmentPokemonBinding
import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.repository.Resource
import com.iamamitbhati.pokedex.ui.adapter.PokemonAdapter
import com.iamamitbhati.pokedex.viewmodel.MainViewModel


class FragmentPokemonList : Fragment() {
    private lateinit var binding: FragmentPokemonBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var pokemonAdapter: PokemonAdapter
    private var pokemons = ArrayList<Pokemon>()
    private var page = 0
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
        initScrollListener()
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
        mainViewModel.stateChange.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    it.data?.let { list ->
                        pokemons.addAll(list)
                        pokemons.takeIf {list-> list.isNotEmpty() }?.let {
                            page = pokemons.last().page + 1
                        }

                    }
                    //pokemonAdapter.setDataList(pokemons)
                    notifyAdapter()
                }
                is Resource.Failed -> {
                    onRetrieveListError(it.errorMessage)
                }
            }
        }
    }

    private fun initScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!binding.progressbar.isVisible) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == pokemons.size - 1) {
                        //bottom of list!
                        loadMore()
                    }
                }
            }
        })
    }

    private fun loadMore() {
        mainViewModel.getPokemons(page)
    }

    /**
     * In order to update complete list
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun notifyAdapter() {
        pokemonAdapter.notifyDataSetChanged()
    }


    private fun onRetrieveListError(errorMessage: String?) {
        val showMessage = errorMessage ?: getString(R.string.post_error)
        val errorSnackbar =
            Snackbar.make(binding.recyclerView, showMessage, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction(R.string.retry, errorClickListener)
        errorSnackbar.show()
    }

    private val errorClickListener = View.OnClickListener { loadMore() }

}