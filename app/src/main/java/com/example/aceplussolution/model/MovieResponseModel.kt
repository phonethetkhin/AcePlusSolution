package com.example.aceplussolution.model

import android.os.Parcelable
import com.example.aceplussolution.roomdb.entity.MovieEntity
import kotlinx.parcelize.Parcelize

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is response model for movie
 */

@Parcelize
data class MovieResponseModel(
    val results: List<MovieEntity>
) : Parcelable

