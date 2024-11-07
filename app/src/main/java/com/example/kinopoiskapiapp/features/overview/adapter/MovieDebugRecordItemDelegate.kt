package com.example.kinopoiskapiapp.features.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoiskapiapp.R
import com.example.kinopoiskapiapp.core.view.adapter.AdapterItemDelegate
import com.example.kinopoiskapiapp.databinding.PartDbmovierecordShortBinding
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi

class MovieDebugRecordItemDelegate: AdapterItemDelegate<MovieUi> {
    override fun forItem(item: MovieUi): Boolean = item is MovieUi.MovieItem

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PartDbmovierecordShortBinding.inflate(layoutInflater, parent, false)
        return MovieDebugRecordItemViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: MovieUi,
        payloads: MutableList<Any>
    ) {
        (viewHolder as MovieDebugRecordItemViewHolder).bind(item as MovieUi.MovieItem)
    }

    class MovieDebugRecordItemViewHolder(private val binding: PartDbmovierecordShortBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieUi.MovieItem) {
            binding.titleName.text = item.name
            Glide.with(itemView.context)
                .load(item.posterUri)
                .centerCrop()
                .error(R.drawable.error_load)
                .into(binding.poster)
        }
    }
}