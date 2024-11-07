package com.example.kinopoiskapiapp.core.data.eventmanager

import com.example.kinopoiskapiapp.core.data.utils.SingleEvent
import com.example.kinopoiskapiapp.core.data.utils.SingleLiveData
import com.example.kinopoiskapiapp.core.data.utils.SingleMutableLiveData
import com.example.kinopoiskapiapp.core.network.models.MovieByIdDto
import javax.inject.Inject

interface FetchMovieByIdEventManager {

    fun getMovieLiveData(): SingleLiveData<MovieByIdDto>
    fun setValue(movieByIdDto: MovieByIdDto)

    class Impl @Inject constructor(): FetchMovieByIdEventManager {

        private val movieByIdLiveData = SingleMutableLiveData<MovieByIdDto>()

        override fun getMovieLiveData(): SingleLiveData<MovieByIdDto> = movieByIdLiveData

        override fun setValue(movieByIdDto: MovieByIdDto) {
            movieByIdLiveData.value = SingleEvent(movieByIdDto)
        }
    }
}