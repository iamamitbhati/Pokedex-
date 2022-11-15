package com.iamamitbhati.pokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iamamitbhati.pokedex.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonList(pokemonList: List<PokemonEntity>)

    @Query("SELECT * FROM ${PokemonEntity.TABLE_NAME} WHERE page = :page_")
    fun getPokemonList(page_: Int): List<PokemonEntity>

    @Query("SELECT * FROM ${PokemonEntity.TABLE_NAME} WHERE page = :page_")
    fun getAllPokemonList(page_: Int): List<PokemonEntity>


}