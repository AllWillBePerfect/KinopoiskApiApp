package com.example.kinopoiskapiapp.features.overview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoiskapiapp.R
import com.example.kinopoiskapiapp.core.view.adapter.AdapterItemDelegate
import com.example.kinopoiskapiapp.databinding.PartMovieShortBinding
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi

class MovieItemDelegate(
    private val onItemClick: (MovieUi.MovieItem) -> Unit
): AdapterItemDelegate<MovieUi>, OnClickListener {

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.partMovieShort) {
            val item = p0.tag as MovieUi.MovieItem
            onItemClick.invoke(item)
        }
    }
    override fun forItem(item: MovieUi): Boolean = item is MovieUi.MovieItem

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PartMovieShortBinding.inflate(layoutInflater, parent, false)
        binding.root.setOnClickListener(this)
        return MovieItemViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: MovieUi,
        payloads: MutableList<Any>
    ) {
        (viewHolder as MovieItemViewHolder).bind(item as MovieUi.MovieItem)
    }

    class MovieItemViewHolder(private val binding: PartMovieShortBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieUi.MovieItem) {
            Glide.with(itemView.context)
                .load(item.posterUri)
                .centerCrop()
                .error(R.drawable.error_load)
                .into(binding.poster)
            binding.name.text = item.name
            binding.number.text = (adapterPosition + 1).toString()
            binding.root.tag = item
        }
    }

}