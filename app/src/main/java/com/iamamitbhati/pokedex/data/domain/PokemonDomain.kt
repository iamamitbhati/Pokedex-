package com.iamamitbhati.pokedex.data.domain

import com.iamamitbhati.pokedex.model.Pokemon
import com.iamamitbhati.pokedex.model.PokemonEntity

fun List<PokemonEntity>.toDomain(): List<Pokemon> {
    return return this.map { pokemonEntity ->
        Pokemon(
            page = pokemonEntity.page,
            name = pokemonEntity.name,
            url = pokemonEntity.url,
            isFav = pokemonEntity.favorite
        )
    }
}

fun List<Pokemon>.toEntity(): List<PokemonEntity> {
     return this.map { pokemon ->
        PokemonEntity(
            page = pokemon.page,
            name = pokemon.name,
            url = pokemon.url,
            favorite = pokemon.isFav
        )
    }
}