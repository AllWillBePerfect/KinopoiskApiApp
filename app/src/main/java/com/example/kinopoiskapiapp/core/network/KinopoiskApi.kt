package com.example.kinopoiskapiapp.core.network

import com.example.kinopoiskapiapp.core.network.models.MovieByIdDto
import com.example.kinopoiskapiapp.core.network.models.MovieDto
import com.example.kinopoiskapiapp.core.network.models.MoviesDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {

    @GET("$GET_URI_PART{$QUERY_PARAM_ID}")
    fun getMovieById(@Path(QUERY_PARAM_ID) id: String): Single<MovieByIdDto>

    @GET(QUERY_PARAM_BY_NAME)
    fun getMoviesByName(
        @Query(QUERY_PARAM_PAGE) page: Int,
        @Query(QUERY_PARAM_LIMIT) limit: Int,
        @Query(QUERY_PARAM_QUERY) query: String
    ): Single<MoviesDto>

    companion object {
        const val GET_URI_PART = "movie/"
        const val QUERY_PARAM_ID = "id"
        const val QUERY_PARAM_BY_NAME = "movie/search"
        const val QUERY_PARAM_PAGE = "page"
        const val QUERY_PARAM_LIMIT = "limit"
        const val QUERY_PARAM_QUERY = "query"
    }
}