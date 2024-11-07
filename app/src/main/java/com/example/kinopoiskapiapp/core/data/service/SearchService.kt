package com.example.kinopoiskapiapp.core.data.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kinopoiskapiapp.core.data.utils.Result
import com.example.kinopoiskapiapp.core.data.utils.SingleEvent
import com.example.kinopoiskapiapp.core.data.utils.SingleLiveData
import com.example.kinopoiskapiapp.core.data.utils.SingleMutableLiveData
import com.example.kinopoiskapiapp.core.network.KinopoiskApi
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

interface SearchService {

    fun setText(charSequence: CharSequence)
    fun getText(): CharSequence
    fun isLoading(): Boolean?
    
    fun getMoviesLiveData(): LiveData<Result<MoviesDto>>
    fun setMoviesLiveData(movies: MoviesDto)
    fun setLoadingLiveData()
    
    fun runSubject()
    fun clear()

    class Impl @Inject constructor(
        private val api: KinopoiskApi
    ) : SearchService {
        var currentPage = 0
        private val limit = 10
        private val disposables = CompositeDisposable()

        private val editTextSubject = BehaviorSubject.create<CharSequence>()


        private val _moviesDtoLiveData = MutableLiveData<Result<MoviesDto>>()

        override fun setText(charSequence: CharSequence) = editTextSubject.onNext(charSequence)
        override fun getText(): CharSequence = editTextSubject.value ?: ""
        override fun isLoading(): Boolean? {
            val state = _moviesDtoLiveData.value
            return state is Result.Loading
        }

        override fun getMoviesLiveData(): LiveData<Result<MoviesDto>> = _moviesDtoLiveData
        override fun setMoviesLiveData(movies: MoviesDto) {
            val oldValue = if (_moviesDtoLiveData.value is Result) (_moviesDtoLiveData.value as Result.Success) else null
            val result = if (oldValue == null) movies else movies.copy(docs = oldValue.data.docs + movies.docs)
            _moviesDtoLiveData.value = Result.Success(result)
        }

        override fun setLoadingLiveData() {
            _moviesDtoLiveData.value = (Result.Loading)
        }

        override fun runSubject() {
            editTextSubject
//                .map {
//                    _moviesDtoLiveData.postValue(SingleEvent(Result.Loading))
//                    it
//                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter { query -> query.isNotBlank() }
                .switchMapSingle {
                    api.getMoviesByName(1, 10, it.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .onErrorResumeNext(::handleHttpErrors)
//                        .onErrorReturn { Movies.empty() }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { result ->
                        _moviesDtoLiveData.value = ((Result.Success(result)))
                    },
                    onComplete = {},
                    onError = {}
                )
                .addTo(disposables)
        }

     

        override fun clear() {
            disposables.clear()
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

    }

}