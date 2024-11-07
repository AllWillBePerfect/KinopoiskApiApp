package com.example.kinopoiskapiapp.features.debug

import androidx.lifecycle.ViewModel
import com.example.kinopoiskapiapp.core.database.dao.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val movieDao: MovieDao
) : ViewModel() {

    val dbMoviesLiveData = movieDao.getAllMoviesLiveData()

}