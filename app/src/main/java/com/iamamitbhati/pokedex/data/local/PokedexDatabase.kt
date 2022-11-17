package com.iamamitbhati.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iamamitbhati.pokedex.data.local.dao.PokemonDao
import com.iamamitbhati.pokedex.data.local.dao.PokemonDetailDao
import com.iamamitbhati.pokedex.model.PokemonDetailsEntity
import com.iamamitbhati.pokedex.model.PokemonEntity

@Database(
  entities = [PokemonEntity::class, PokemonDetailsEntity::class],
  version = 1,
  exportSchema = true
)
abstract class PokedexDatabase : RoomDatabase() {

  abstract fun pokemonDao(): PokemonDao
  abstract fun pokemonDetailDao(): PokemonDetailDao
}
