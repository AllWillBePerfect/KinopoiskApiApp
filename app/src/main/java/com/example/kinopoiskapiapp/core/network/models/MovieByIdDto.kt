package com.example.kinopoiskapiapp.core.network.models

import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import com.example.kinopoiskapiapp.core.network.models.MovieDto.PosterDtoPart
import com.google.gson.annotations.SerializedName

data class MovieByIdDto(
    val id: Int?,
    val type: String?,
    val name: String?,
    val description: String?,
    val year: Int?,
    @SerializedName("poster") val posterDtoPart: PosterDtoPart?,
    val genres: List<GenreDtoPart>?,
    val countries: List<CountriesDtoPart>?,
    val persons: List<PersonDtoPart>?,
) {
    fun toDbo(): MovieByIdDbo = MovieByIdDbo(
        id = id?: 0,
        type = type,
        name = name,
        description = description,
        year = year,
        poster = posterDtoPart?.toDbo(),
        genres = genres?.map { it.toDbo() },
        countries = countries?.map { it.toDbo() },
        persons = persons?.map { it.toDbo() }
    )

    data class GenreDtoPart(
        val name: String?
    ) {
        fun toDbo(): MovieByIdDbo.GenreDboPart = MovieByIdDbo.GenreDboPart(
            name = name
        )
    }

    data class CountriesDtoPart(
        val name: String?
    ) {
        fun toDbo(): MovieByIdDbo.CountriesDboPart = MovieByIdDbo.CountriesDboPart(
            name = name
        )
    }

    data class PersonDtoPart(
        val id: Int?,
        val photo: String?,
        val name: String?,
        val enName: String?,
        val description: String?,
        val profession: String?,
        val enProfession: String?
    ) {
        fun toDbo(): MovieByIdDbo.PersonDboPart = MovieByIdDbo.PersonDboPart(
            id = id,
            photo = photo,
            name = name,
            enName = enName,
            description = description,
            profession = profession,
            enProfession = enProfession
        )

    }
}