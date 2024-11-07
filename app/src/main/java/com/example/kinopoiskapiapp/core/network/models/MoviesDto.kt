package com.example.kinopoiskapiapp.core.network.models

data class MoviesDto(
    val docs: List<MovieDto>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
) {
    companion object {
        fun empty() = MoviesDto(emptyList(), 0, 0, 0, 0)
    }
}