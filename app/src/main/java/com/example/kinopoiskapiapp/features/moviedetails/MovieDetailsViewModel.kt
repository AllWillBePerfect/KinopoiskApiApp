package com.example.kinopoiskapiapp.features.moviedetails

import androidx.lifecycle.ViewModel
import com.example.kinopoiskapiapp.core.data.eventmanager.FetchMovieByIdEventManager
import com.example.kinopoiskapiapp.core.database.dao.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val fetchMovieByIdEventManager: FetchMovieByIdEventManager
) : ViewModel() {

    val movieLiveData = fetchMovieByIdEventManager.getMovieLiveData()
}