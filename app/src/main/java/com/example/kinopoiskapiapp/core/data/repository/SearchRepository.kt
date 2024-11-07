package com.example.kinopoiskapiapp.core.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kinopoiskapiapp.core.data.utils.Result
import com.example.kinopoiskapiapp.core.network.KinopoiskApi
import com.example.kinopoiskapiapp.core.network.models.MovieDto
import com.example.kinopoiskapiapp.core.network.models.MoviesDto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface SearchRepository {

    fun runSubjects()
    fun clear()

    fun getMoviesLiveData(): LiveData<Result<MoviesDto>>
    fun isLoading(): Boolean

    fun setText(query: String)
    fun setAction()

    class Impl @Inject constructor(
        private val api: KinopoiskApi
    ) : SearchRepository {

        private val searchStartPage = 1
        private var pagingPage = searchStartPage
        private val limit = 10
        private var moviesSnap: MutableList<MovieDto> = mutableListOf()

        private val editTextSubject = BehaviorSubject.create<String>()
        private val pagingSubject = BehaviorSubject.create<Unit>()

        private val _moviesDtoLiveData = MutableLiveData<Result<MoviesDto>>(Result.Loading)


        private val disposables = CompositeDisposable()
        override fun runSubjects() {
            editTextSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter { query -> query.isNotBlank() }
                .switchMapSingle(::firstRequestMovies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        _moviesDtoLiveData.value = Result.Success(it)
                        moviesSnap = it.docs.toMutableList()
                        pagingPage = 2
                    },
                    onComplete = {},
                    onError = {
                        _moviesDtoLiveData.value = Result.Error(it)
                    },
                ).addTo(disposables)

            pagingSubject
                .switchMapSingle(::pagingRequestMovies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        moviesSnap.addAll(it.docs)
                        val movies = it.copy(docs = moviesSnap)
                        _moviesDtoLiveData.value = Result.Success(movies)
                        pagingPage++
                    },
                    onComplete = {},
                    onError = {
                        _moviesDtoLiveData.value = Result.Error(it)
                    },
                ).addTo(disposables)

        }

        override fun clear() {
            disposables.clear()
        }

        override fun getMoviesLiveData(): LiveData<Result<MoviesDto>> = _moviesDtoLiveData
        override fun isLoading(): Boolean = _moviesDtoLiveData.value is Result.Loading


        override fun setText(query: String) {
            _moviesDtoLiveData.value = Result.Loading
            editTextSubject.onNext(query)
        }

        override fun setAction() {
            _moviesDtoLiveData.value = Result.Loading
            pagingSubject.onNext(Unit)
        }

        private fun firstRequestMovies(query: String): Single<MoviesDto> =
            api.getMoviesByName(searchStartPage, limit, query)
                .onErrorResumeNext(::handleHttpErrors)
                .subscribeOn(Schedulers.io())

        private fun pagingRequestMovies(action: Unit): Single<MoviesDto> =
            api.getMoviesByName(pagingPage, limit, editTextSubject.value ?: "")
                .onErrorResumeNext(::handleHttpErrors)
                .subscribeOn(Schedulers.io())


        private fun <T> handleHttpErrors(throwable: Throwable): Single<T> {
            return when (throwable) {
                is HttpException -> {
                    when (throwable.code()) {
                        401 -> Single.error(Throwable("Ошибка 401: Неавторизован"))
                        402 -> Single.error(Throwable("Ошибка 402: Требуется оплата"))
                        403 -> Single.error(Throwable("Ошибка 403: Токен исчерпан"))
                        else -> Single.error(throwable)
                    }
                }

                else -> Single.error(throwable)
            }
        }
    }
}