package com.example.aceplussolution.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aceplussolution.repository.MovieRepository
import com.example.aceplussolution.roomdb.MovieDB
import com.example.aceplussolution.roomdb.entity.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is MovieViewModel for manipulation of movie
 */

class MovieViewModel(private val movieRepository: MovieRepository, private val movieDB: MovieDB) :
    ViewModel() {

    val movieListLiveData = MutableLiveData<ArrayList<MovieEntity>>()
    val movieList = ArrayList<MovieEntity>()

    var localMovieListLiveData: LiveData<List<MovieEntity>>? = null


    /**
     * This function is getting the movieList from the API.
     * 1. fetch the popular movies from the API and wait for it to finish.
     * 2. fetch the upcoming movies from the API and wait for it to finish.
     * 3. combine both of them and add it into the list.
     * 4. if movieList not empty, delete and insert it all and update the fav status if any exist.
     * 5. post it as live data and return it to the UI
     */
    fun getMovieList() = viewModelScope.launch {
        val popularMovies = withContext(Dispatchers.IO) { movieRepository.getPopularMovies() }
        val upcomingMovies = withContext(Dispatchers.IO) { movieRepository.getUpcomingMovies() }

        movieList.addAll(popularMovies)
        movieList.addAll(upcomingMovies)

        if (!(movieList.isNullOrEmpty())) {
            val previousLocalMovies = movieDB.movieDao().getAllMovies()  //getPreviousLocalMovies

            val favList =
                previousLocalMovies.filter { it.favorite != 0 } //get Favorite Movies List if exist

            movieDB.movieDao().deleteAllMovies() //delete all movies from the database

            val movies: List<MovieEntity> = movieList
            movieDB.movieDao().insertAllMovies(movies)  //insert it all again with new data

            if (favList.isNotEmpty()) {  //if favorite exist
                favList.forEach {
                    val currentMovies = movieDB.movieDao().getAllMovies() //get current arraylist

                    val isMovieExist =
                        currentMovies.find { movie -> movie.id == it.id } //check the current movie is exist or not in the current movies with it's id

                    if (isMovieExist != null)
                        movieDB.movieDao().updateFav(
                            1,
                            it.id
                        ) //if movie is exist and it also have favorite status, update again on the new data
                }
            }
        }
        movieListLiveData.postValue(movieList)
    }

    /**
     * This function is getting the live data of movie from local db
     */
    fun getMovieDBLiveData() = viewModelScope.launch {
        localMovieListLiveData = movieRepository.getMoviesFromDBLiveData()
    }

    /**
     * This function is update the fav status of movie
     */
    fun updateFav(status: Int, id: Int) = viewModelScope.launch {
        movieRepository.updateFav(status, id)
    }

    /**
     * This function is getting the fav status of movie.
     */
    suspend fun getFav(id: Int): Int? {
        return movieRepository.getFav(id)
    }
}