package com.example.kinopoiskapiapp.features.overview.deprecated.v1

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kinopoiskapiapp.core.data.eventmanager.FetchMovieByIdEventManager
import com.example.kinopoiskapiapp.core.data.service.SearchService
import com.example.kinopoiskapiapp.core.data.utils.Result
import com.example.kinopoiskapiapp.core.data.utils.SingleEvent
import com.example.kinopoiskapiapp.core.data.utils.SingleLiveData
import com.example.kinopoiskapiapp.core.data.utils.SingleMutableLiveData
import com.example.kinopoiskapiapp.core.database.dao.MovieDao
import com.example.kinopoiskapiapp.core.network.KinopoiskApi
import com.example.kinopoiskapiapp.core.network.models.MovieByIdDto
import com.example.kinopoiskapiapp.core.network.models.MovieDto
import com.example.kinopoiskapiapp.core.network.models.MoviesDto
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@Deprecated("")
@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val api: KinopoiskApi,
    private val searchService: SearchService,
    private val movieDao: MovieDao,
    private val fetchMovieByIdEventManager: FetchMovieByIdEventManager
) : ViewModel() {

    private val disposables = CompositeDisposable()

    val searchResultLiveData: LiveData<Result<MoviesDto>> = searchService.getMoviesLiveData()

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    private val _moviesLiveData = MutableLiveData<List<MovieUi>>()
    val moviesLiveData: LiveData<List<MovieUi>> = _moviesLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    private val _navigateViewModel = SingleMutableLiveData<Boolean>()
    val navigateViewModel: SingleLiveData<Boolean> = _navigateViewModel

    var currentPage = 0
    private val limit = 10

    private val observer = observer()

    init {
        searchService.runSubject()
        searchResultLiveData.observeForever(observer)
    }

    fun setText(charSequence: CharSequence) = searchService.setText(charSequence)

    fun loadMoreData(isScrollingDown: Boolean) {
        disposables.clear()
        val pageToLoad = if (isScrollingDown) currentPage + 1 else currentPage - 1

        api.getMoviesByName(pageToLoad, limit, searchService.getText().toString())
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext(::handleHttpErrors)
            .observeOn(Schedulers.computation())
            .map { map(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoadingLiveData.value = true }
            .doFinally { _isLoadingLiveData.value = false }
            .subscribeBy(
                onSuccess = { movies ->
                    if (isScrollingDown) {
                        _moviesLiveData.value = _moviesLiveData.value.orEmpty() + movies
                        currentPage++
                    } else {
                        _moviesLiveData.value = _moviesLiveData.value.orEmpty() + movies
                        currentPage--
                    }
                },
                onError = { error ->
                    _errorLiveData.value = error
                }
            ).addTo(disposables)
        Log.d("ViewModel currentPage", currentPage.toString())
    }

    fun fetchMovieById(movieItem: MovieUi.MovieItem) {
        disposables.clear()
        api.getMovieById(movieItem.id.toString())
            .onErrorResumeNext(::handleHttpErrors)
//            .flatMap(::saveToDb)
            .doOnSuccess { movieDao.insertMovie(it.toDbo()).subscribeOn(Schedulers.io()).subscribe() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { movie ->
                    fetchMovieByIdEventManager.setValue(movie)
                    navigateToMovieDetails()
                    Log.d("ViewModel fetched movie", movie.toString())
                },
                onError = { error ->
                    _errorLiveData.value = error
                }
            ).addTo(disposables)
    }

    private fun navigateToMovieDetails() {_navigateViewModel.value = SingleEvent(true)}

    fun getLoadingState(): Boolean? = _isLoadingLiveData.value

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

    private fun <T> handleHttpErrors(throwable: Throwable): Single<T> {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    401 -> Single.error(Throwable("Ошибка 401: Неавторизован"))
                    402 -> Single.error(Throwable("Ошибка 402: Требуется оплата"))
                    403 -> Single.error(Throwable("Ошибка 403: Токен исчерпан"))
                    else -> Single.error(throwable) // Другие HTTP ошибки
                }
            }

            else -> Single.error(throwable) // Ошибки, не относящиеся к HTTP
        }
    }

    private fun saveToDb(movie: MovieByIdDto): Single<MovieByIdDto> {
        return Single.fromCallable {
            try {
                val item = movie.toDbo()
                movieDao.insertMovie(item)
                Log.d("ViewModel saved movie", movie.toString())
                movie
            } catch (e: Exception) {
                Log.e("ViewModel saveToDb error", "Error saving movie", e)
                throw e
            }
        }
            .subscribeOn(Schedulers.io())
    }

    private fun observer(): (value: Result<MoviesDto>) -> Unit =
        {
            it.let { value ->
                when (value) {
                    is Result.Success -> {
                        _isLoadingLiveData.value = false
                        currentPage = 0
                        _moviesLiveData.value = value.data.docs.map { map(it) }
                    }

                    is Result.Error -> {
                        _isLoadingLiveData.value = false
                        _errorLiveData.value = value.exception
                    }

                    is Result.Loading -> {
                        _isLoadingLiveData.value = true
                    }
                }
            }
        }

    override fun onCleared() {
        disposables.dispose()
        searchService.clear()
        searchResultLiveData.removeObserver(observer)
        super.onCleared()
    }
}