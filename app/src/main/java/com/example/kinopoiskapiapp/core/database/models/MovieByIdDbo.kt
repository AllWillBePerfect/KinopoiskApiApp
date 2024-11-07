package com.example.kinopoiskapiapp.core.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "movies")
data class MovieByIdDbo(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val type: String?,
    val name: String?,
    val description: String?,
    val year: Int?,
    val poster: PosterDboPart?,
    val genres: List<GenreDboPart>?,
    val countries: List<CountriesDboPart>?,
    val persons: List<PersonDboPart>?

) {

    data class PosterDboPart(
        val url: String?,
        val previewUrl: String?
    )

    data class GenreDboPart(
        val name: String?
    )

    data class CountriesDboPart(
        val name: String?
    )

    data class PersonDboPart(
        val id: Int?,
        val photo: String?,
        val name: String?,
        val enName: String?,
        val description: String?,
        val profession: String?,
        val enProfession: String?
    )

    companion object {
        fun empty(id: Int = 0): MovieByIdDbo = MovieByIdDbo(
            id = id,
            type = null,
            name = null,
            description = null,
            year = null,
            poster = null,
            genres = null,
            countries = null,
            persons = null
        )
    }
}
