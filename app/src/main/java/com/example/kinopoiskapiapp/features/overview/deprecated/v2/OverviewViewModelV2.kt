package com.example.kinopoiskapiapp.features.overview.deprecated.v2

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.kinopoiskapiapp.core.data.eventmanager.FetchMovieByIdEventManager
import com.example.kinopoiskapiapp.core.data.service.SearchService
import com.example.kinopoiskapiapp.core.data.utils.Result
import com.example.kinopoiskapiapp.core.data.utils.SingleEvent
import com.example.kinopoiskapiapp.core.data.utils.SingleLiveData
import com.example.kinopoiskapiapp.core.database.dao.MovieDao
import com.example.kinopoiskapiapp.core.network.KinopoiskApi
import com.example.kinopoiskapiapp.core.network.models.MovieDto
import com.example.kinopoiskapiapp.core.network.models.MoviesDto
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@Deprecated("")
@HiltViewModel
class OverviewViewModelV2 @Inject constructor(
    private val api: KinopoiskApi,
    private val searchService: SearchService,
    private val movieDao: MovieDao,
    private val fetchMovieByIdEventManager: FetchMovieByIdEventManager
) : ViewModel() {

    val moviesLiveData: SingleLiveData<Result<List<MovieUi>>> =
        searchService.getMoviesLiveData().switchMap { ld ->
            val value = when (val event = ld) {
                is Result.Success -> {
                    currentPage = 0
                    Result.Success(map(event.data))
                }

                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(event.exception)
            }
            MutableLiveData(SingleEvent(value))
        }

    //    private val moviesSubject = BehaviorSubject.create<List<MovieUi>>()
//    private val selectedMovieSubject = BehaviorSubject.create<MovieUi?>()
    var currentPage = 0
    private val limit = 10
    private val pagingSubject = BehaviorSubject.create<Unit>()
    private val disposables = CompositeDisposable()

    init {

        searchService.runSubject()

        pagingSubject
            .map { currentPage + 1 }
            .switchMapSingle {
                searchService.setLoadingLiveData()
                api.getMoviesByName(it, limit, searchService.getText().toString())
                    .subscribeOn(Schedulers.io())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { movies ->
                    searchService.setMoviesLiveData(movies)
                    currentPage++
                },
                onComplete = {},
                onError = {},
            )
            .addTo(disposables)

//        Observable.combineLatest(moviesSubject, selectedMovieSubject) { list, movie ->
//
//        }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onNext = {},
//                onComplete = {},
//                onError = {},
//            )
//            .addTo(disposables)
    }

    fun setText(toString: String) = searchService.setText(toString)

    private fun map(dto: MoviesDto): List<MovieUi> {
        Log.d("ViewModel total movies", dto.total.toString())
        return dto.docs.map {
            MovieUi.MovieItem(
                id = it.id,
                posterUri = Uri.parse(it.posterDtoPart?.url ?: ""),
                name = it.name
            )
        }
    }

    private fun map(movieDto: MovieDto): MovieUi {
        Log.d("ViewModel total movies", movieDto.toString())
        return MovieUi.MovieItem(
            id = movieDto.id,
            posterUri = Uri.parse(movieDto.posterDtoPart?.url ?: ""),
            name = movieDto.name
        )

    }

    fun fetchMovieById(movieItem: MovieUi.MovieItem) {

    }

    fun getLoadingState(): Boolean? = searchService.isLoading()
    fun loadMoreData() {
        pagingSubject.onNext(Unit)
    }


    override fun onCleared() {
        disposables.dispose()
        searchService.clear()
        super.onCleared()
    }


}