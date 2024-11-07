package com.example.kinopoiskapiapp.core.network.models

import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import com.google.gson.annotations.SerializedName

data class MovieDto(
    val id: Int,
    val type: String,
    val name: String,
    val year: Int,
    @SerializedName("poster") val posterDtoPart: PosterDtoPart?,
) {

    data class PosterDtoPart(
        val url: String?,
        val previewUrl: String?
    ) {
        fun toDbo() = MovieByIdDbo.PosterDboPart(url, previewUrl)
    }
}
