package com.example.kinopoiskapiapp.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieByIdDbo): Single<Long>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): Single<MovieByIdDbo>

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Single<List<MovieByIdDbo>>

    @Query("SELECT * FROM movies")
    fun getAllMoviesFlowable(): Flowable<List<MovieByIdDbo>>

    @Query("SELECT * FROM movies")
    fun getAllMoviesLiveData(): LiveData<List<MovieByIdDbo>>

    @Query("DELETE FROM movies")
    fun nukeTable(): Completable



}