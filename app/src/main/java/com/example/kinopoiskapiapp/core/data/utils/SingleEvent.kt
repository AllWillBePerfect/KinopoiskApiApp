package com.example.kinopoiskapiapp.core.data.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface LiveDataIntermediate<T> {
    var event: T?
}

class SingleEvent<T>(value: T?): LiveDataIntermediate<T> {
    override var event: T? = value
        get() = field.also {
            event = null
            eventForCheck = null
        }

    var eventForCheck = value
}

typealias SingleLiveData<T> = LiveData<SingleEvent<T>>
typealias SingleMutableLiveData<T> = MutableLiveData<SingleEvent<T>>