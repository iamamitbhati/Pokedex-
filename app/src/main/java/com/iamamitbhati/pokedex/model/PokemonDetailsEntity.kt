package com.iamamitbhati.pokedex.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.iamamitbhati.pokedex.model.PokemonDetailsEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class PokemonDetailsEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    @SerializedName("base_experience")
    val experience: Int
) {
    companion object {
        const val TABLE_NAME = "pokemon_detail_entity"
    }
}
