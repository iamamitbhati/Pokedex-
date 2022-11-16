package com.iamamitbhati.pokedex.model


data class Pokemon(
  var page: Int = 0,
  val name: String,
  val url: String,
  var isFav: Boolean=false
) {

  fun getImageUrl(): String {
    val index = url.split("/".toRegex()).dropLast(1).last()
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
  }
}
