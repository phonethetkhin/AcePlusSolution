package com.example.aceplussolution.repository

import android.content.Context
import com.example.aceplussolution.common.API_KEY
import com.example.aceplussolution.roomdb.MovieDB
import com.example.aceplussolution.roomdb.entity.MovieEntity
import com.example.aceplussolution.service.APIService
import com.example.aceplussolution.utility.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is repository class which fetch data from API & room
 */
class MovieRepository(
    private val context: Context,
    val apiService: APIService,
    val movieDB: MovieDB
) {

    /**
     * This function is fetching the popular movie from the API
     * if response is success, return the arraylist of movie entity. else show the message accordingly
     */
    suspend fun getPopularMovies(): ArrayList<MovieEntity> {
        val popularMovieList = ArrayList<MovieEntity>()
        try {
            val response = apiService.getPopularMovies(API_KEY, 1)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    popularMovieList.addAll(response.body()!!.results)
                }
            } else {
                withContext(Dispatchers.Main) { context.showToast("Some error occurred when fetching popular movies.") }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                if (e is IOException)
                    context.showToast("No internet connection, please check your mobile data or wifi")
                else
                    context.showToast("Some Error Occurred, ${e.localizedMessage}")
            }
        }
        return popularMovieList
    }

    /**
     * This function is fetching the upcoming movie from the API
     * if response is success, return the arraylist of movie entity. else show the message accordingly
     */
    suspend fun getUpcomingMovies(): ArrayList<MovieEntity> {
        val movieModelList = ArrayList<MovieEntity>()
        try {
            val response = apiService.getUpcomingMovies(API_KEY, 1)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    movieModelList.addAll(response.body()!!.results)
                }
            } else {
                withContext(Dispatchers.Main) { context.showToast("Some error occurred when fetching upcoming movies.") }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                if (e is IOException)
                    context.showToast("No internet connection, please check your mobile data or wifi")
                else
                    context.showToast("Some Error Occurred, ${e.localizedMessage}")
            }
        }

        return movieModelList
    }

    /**
     * This function is fetching movie live data from the room database
     * if something is change inside database, this will invoke.
     */
    fun getMoviesFromDBLiveData() =
        movieDB.movieDao().getAllMoviesLiveData()

    /**
     * This function is used to update the favorite status of movie entity.
     * it will update movie favorite status with movie id.
     */
    suspend fun updateFav(status: Int, id: Int) {
        movieDB.movieDao().updateFav(status, id)
    }

    /**
     * This function is get the favorite status of the movie.
     * return the fav status of movie checked by it's id.
     */
    suspend fun getFav(id: Int): Int? {
        return movieDB.movieDao().getFavStatus(id)
    }
}