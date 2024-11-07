package com.example.kinopoiskapiapp.core.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class UniversalRecyclerViewAdapter<T>(
    private val delegates: List<AdapterItemDelegate<T>>,
    private val diffUtilCallback: CustomDiffUtilCallback<T>
) : RecyclerView.Adapter<ViewHolder>() {

    var items: List<T> = emptyList()
        get() = field
        set(value) {
            val sd = DelegateDiffResult(diffUtilCallback)
            val diffResult = DiffUtil.calculateDiff(sd.getCallback(field, value))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        delegates[viewType].getViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        delegates[getItemViewType(position)].bindViewHolder(
            holder,
            items[position],
            mutableListOf()
        )

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        delegates.indexOfFirst { delegate -> delegate.forItem(items[position]) }


}
