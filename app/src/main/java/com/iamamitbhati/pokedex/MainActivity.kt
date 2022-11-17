package com.iamamitbhati.pokedex


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.iamamitbhati.pokedex.databinding.ActivityMainBinding
import com.iamamitbhati.pokedex.ui.FragmentPokemonListDirections
import com.iamamitbhati.pokedex.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var activityMainBinding: ActivityMainBinding
    private var menu :Menu?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        mainViewModel.getPokemons(page = 0)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.fav_menu, menu)
        this.menu= menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fav -> {
                showFavorite()
                makeMenuUnavailable()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun makeMenuUnavailable() {
       menu?.clear()
    }

    private fun showFavorite() {
        val directions = FragmentPokemonListDirections.navigationToFavPokemon()
        activityMainBinding.navHostFragment.findNavController().navigate(directions)
    }


}