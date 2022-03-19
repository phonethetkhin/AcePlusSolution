package com.example.aceplussolution.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aceplussolution.R

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is the Main Activity.
 * It's uses nothing in class, but to host default nav host(movieListFragment)
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}