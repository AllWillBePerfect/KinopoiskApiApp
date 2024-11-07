package com.example.kinopoiskapiapp.core.database.converters

import androidx.room.TypeConverter
import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class KinopoiskTypeConverters {

    val gson = Gson()

    @TypeConverter
    fun fromPosterToTable(value: String?): MovieByIdDbo.PosterDboPart {

        return gson.fromJson(value, MovieByIdDbo.PosterDboPart::class.java)
            ?: MovieByIdDbo.PosterDboPart(null, null)
    }

    @TypeConverter
    fun fromTableToPoster(value: MovieByIdDbo.PosterDboPart?): String? = gson.toJson(value)

    @TypeConverter
    fun fromGenresToTable(value: String?): List<MovieByIdDbo.GenreDboPart> {
        return gson.fromJson(
            value,
            object : TypeToken<List<MovieByIdDbo.GenreDboPart>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun fromTableToGenres(value: List<MovieByIdDbo.GenreDboPart>?): String? = gson.toJson(value)

    @TypeConverter
    fun fromCountriesToTable(value: String?): List<MovieByIdDbo.CountriesDboPart> {
        return gson.fromJson(
            value,
            object : TypeToken<List<MovieByIdDbo.CountriesDboPart>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun fromTableToCountries(value: List<MovieByIdDbo.CountriesDboPart>?): String? =
        gson.toJson(value)

    @TypeConverter
    fun fromPersonsToTable(value: String?): List<MovieByIdDbo.PersonDboPart> {
        return gson.fromJson(
            value,
            object : TypeToken<List<MovieByIdDbo.PersonDboPart>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun fromTableToPersons(value: List<MovieByIdDbo.PersonDboPart>?): String? = gson.toJson(value)

}