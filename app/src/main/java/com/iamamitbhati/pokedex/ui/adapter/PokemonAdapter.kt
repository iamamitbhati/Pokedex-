package com.iamamitbhati.pokedex.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iamamitbhati.pokedex.databinding.ItemPokemonBinding
import com.iamamitbhati.pokedex.model.Pokemon


class PokemonAdapter(
    val dataSet: ArrayList<Pokemon>,
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
        holder.bind(pokemon, onItemClick)
    }

    class AdapterViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon, onItemClick: ((Pokemon) -> Unit)?) {
            with(binding) {
                name.text = pokemon.name
                Glide.with(root.context)
                    .load(pokemon.getImageUrl())
                    .into(image)
                root.setOnClickListener {
                    onItemClick?.invoke(pokemon)
                }
            }

        }
    }
}