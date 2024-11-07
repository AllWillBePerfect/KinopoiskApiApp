package com.example.kinopoiskapiapp.features.overview.v3

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.kinopoiskapiapp.core.data.eventmanager.FetchMovieByIdEventManager
import com.example.kinopoiskapiapp.core.data.repository.SearchRepository
import com.example.kinopoiskapiapp.core.data.utils.Result
import com.example.kinopoiskapiapp.core.data.utils.SingleEvent
import com.example.kinopoiskapiapp.core.data.utils.SingleLiveData
import com.example.kinopoiskapiapp.core.data.utils.SingleMutableLiveData
import com.example.kinopoiskapiapp.core.database.dao.MovieDao
import com.example.kinopoiskapiapp.core.network.models.MoviesDto
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModelV3 @Inject constructor(
    private val searchRepository: SearchRepository,
    private val movieDao: MovieDao,
    private val fetchMovieByIdEventManager: FetchMovieByIdEventManager
) : ViewModel() {

    val moviesLiveData: LiveData<Result<List<MovieUi>>> =
        searchRepository.getMoviesLiveData().switchMap { searchLiveData ->
            val value = when (searchLiveData) {
                is Result.Success -> {
                    Result.Success(map(searchLiveData.data))
                }

                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(searchLiveData.exception)
            }
            MutableLiveData(value)
        }

    private val _navigateViewModel = SingleMutableLiveData<Boolean>()
    val navigateViewModel: SingleLiveData<Boolean> = _navigateViewModel

    init {
        searchRepository.runSubjects()
    }

    fun firstFetchMovies(toString: String) = searchRepository.setText(toString)
    fun nextPageFetchMovies() = searchRepository.setAction()
    private fun navigateToMovieDetails() {_navigateViewModel.value = SingleEvent(true) }

    fun isLoading() = searchRepository.isLoading()

    private fun map(dto: MoviesDto): List<MovieUi> {
        Log.d("ViewModel map", "total movies: ${dto.total} \n" +
                "page: ${dto.page}")
        return dto.docs.map {
            MovieUi.MovieItem(
                id = it.id,
                posterUri = Uri.parse(it.posterDtoPart?.url ?: ""),
                name = it.name
            )
        }
    }

    fun fetchMovieById(movieItem: MovieUi.MovieItem) {

    }

    override fun onCleared() {
        searchRepository.clear()
        super.onCleared()
    }


}