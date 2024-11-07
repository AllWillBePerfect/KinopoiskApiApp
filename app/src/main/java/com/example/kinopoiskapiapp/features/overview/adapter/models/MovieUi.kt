package com.example.kinopoiskapiapp.features.overview.adapter.models

import android.net.Uri
import com.example.kinopoiskapiapp.core.view.adapter.CustomDiffUtilCallback

sealed class MovieUi {
    data class MovieItem(val id: Int, val posterUri: Uri, val name: String) : MovieUi()

    class MovieItemDiffUtil: CustomDiffUtilCallback<MovieUi> {
        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<MovieUi>,
            newList: List<MovieUi>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem is MovieItem && newItem is MovieItem)
                oldItem.name == newItem.name && oldItem.posterUri == newItem.posterUri && oldItem.id == newItem.id
            else false
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<MovieUi>,
            newList: List<MovieUi>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<MovieUi>,
            newList: List<MovieUi>
        ): Any? = null
    }
}