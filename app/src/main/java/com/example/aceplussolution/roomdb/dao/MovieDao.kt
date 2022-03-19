package com.example.aceplussolution.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aceplussolution.roomdb.entity.MovieEntity

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is Dao class for movie table
 */
@Dao
interface MovieDao {

    /**
     * This function is inserting all movies from the API into the database
     * if conflict occur, it will replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: Iterable<MovieEntity>)

    /**
     * This function is get all movies from the room database in order check in the background and to store previous list
     */
    @Query("SELECT * FROM tbl_movie")
    suspend fun getAllMovies(): List<MovieEntity>

    /**
     * This function is get movie livedata from the room database in order to show the updated list to the user.
     */
    @Query("SELECT * FROM tbl_movie")
    fun getAllMoviesLiveData(): LiveData<List<MovieEntity>>

    /**
     * This function is used to update the favorite status of movie entity.
     * it will update movie favorite status with movie id.
     */
    @Query("UPDATE tbl_movie SET fav=:status WHERE id =:id")
    suspend fun updateFav(status: Int, id: Int)

    /**
     * This function is get the favorite status of the movie.
     * return the fav status of movie checked by it's id.
     */
    @Query("SELECT fav FROM tbl_movie WHERE id=:id")
    suspend fun getFavStatus(id: Int): Int?

    /**
     * This function is used to delete all movies from the room database.
     */
    @Query("DELETE FROM tbl_movie")
    suspend fun deleteAllMovies()

}