package com.iamamitbhati.pokedex.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iamamitbhati.pokedex.R
import com.iamamitbhati.pokedex.databinding.ItemPokemonBinding
import com.iamamitbhati.pokedex.model.Pokemon
import okhttp3.internal.notify


class PokemonAdapter(
    val dataSet: ArrayList<Pokemon>,
    var onFavClick: ((Pokemon, Boolean, Int) -> Unit)? = null,
    var onItemClick: ((Pokemon) -> Unit)? = null
) :
    RecyclerView.Adapter<PokemonAdapter.AdapterViewHolder>() {

    //private var dataSet: List<Pokemon> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPokemonBinding.inflate(inflater, parent, false)
        return AdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val pokemon = dataSet[position]
        holder.bind(pokemon,onFavClick, onItemClick)
    }

    class AdapterViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon,onFavClick: ((Pokemon, Boolean, Int) -> Unit)?, onItemClick: ((Pokemon) -> Unit)?) {
            with(binding) {
                name.text = pokemon.name
                Glide.with(root.context)
                    .load(pokemon.getImageUrl())
                    .into(image)
                root.setOnClickListener {
                    onItemClick?.invoke(pokemon)
                }

                favImage.setOnClickListener {
                    onFavClick?.invoke(pokemon, !pokemon.isFav, adapterPosition)
                    pokemon.isFav= !pokemon.isFav
                }
                val drawable = pokemon.takeIf { it.isFav }?.let {
                    R.drawable.ic_favorite
                } ?: kotlin.run {
                    R.drawable.ic_not_favorite
                }

                val isFavDrawable= ContextCompat.getDrawable(binding.root.context,drawable)
                favImage.setImageDrawable(isFavDrawable)
            }

        }
    }
}