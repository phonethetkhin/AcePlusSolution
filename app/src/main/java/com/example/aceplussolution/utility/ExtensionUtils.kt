package com.example.aceplussolution.utility

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is the Extension Utility class
 */

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is function to show Toast
 */
fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is delegate function to retrieve instance of ViewModel from Activity
 */
inline fun <reified VM : ViewModel, T> T.kodeinViewModel(): Lazy<VM> where T : DIAware, T : AppCompatActivity {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is delegate function to retrieve instance of ViewModel from Fragment
 */
inline fun <reified VM : ViewModel, T> T.kodeinViewModel(): Lazy<VM> where T : DIAware, T : Fragment {
    return lazy { ViewModelProvider(requireActivity(), direct.instance()).get(VM::class.java) }
}

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is the function of setting the boolean value
 */
fun Context.setBooleanPref(name: String, key: String, value: Boolean) {
    val pref = this.getSharedPreferences(name, Context.MODE_PRIVATE)
    val editor = pref.edit()
    editor.putBoolean(key, value)
    editor.apply()
}

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is the function of getting the boolean value
 */
fun Context.getBooleanPref(name: String, key: String, defaultValue: Boolean): Boolean {
    val pref = this.getSharedPreferences(name, Context.MODE_PRIVATE)
    return pref.getBoolean(key, defaultValue)
}


