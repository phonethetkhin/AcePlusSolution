package com.example.aceplussolution.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aceplussolution.roomdb.dao.MovieDao
import com.example.aceplussolution.roomdb.entity.MovieEntity

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is the database class for movie
 */
@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)

abstract class MovieDB : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDB? = null
        fun getInstance(context: Context): MovieDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MovieDB::class.java, "AcePlus.db"
            ).build()
    }
}