package com.example.kinopoiskapiapp.core.view.adapter

import androidx.recyclerview.widget.DiffUtil.Callback

class DelegateDiffResult<T>(
    private val delegate: CustomDiffUtilCallback<T>,
)  {
    fun getCallback(oldList: List<T>, newList: List<T>): Callback {
        return object : Callback() {
            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                delegate.areItemsTheSame(oldItemPosition, newItemPosition, oldList, newList)

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                delegate.areContentsTheSame(oldItemPosition, newItemPosition, oldList, newList)

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
                delegate.getChangePayload(oldItemPosition, newItemPosition, oldList, newList)

        }
    }
}

interface CustomDiffUtilCallback<T> {
    fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int, oldList: List<T>, newList: List<T>): Boolean
    fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int, oldList: List<T>, newList: List<T>): Boolean
    fun getChangePayload(oldItemPosition: Int, newItemPosition: Int, oldList: List<T>, newList: List<T>): Any?
}