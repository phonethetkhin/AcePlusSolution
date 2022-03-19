package com.example.aceplussolution.application

import android.app.Application
import com.example.aceplussolution.repository.MovieRepository
import com.example.aceplussolution.roomdb.MovieDB
import com.example.aceplussolution.service.RetrofitObj
import com.example.aceplussolution.utility.ViewModelFactory
import com.example.aceplussolution.viewmodel.MovieViewModel
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is application class of this application and it store the dependency here.
 */
class AcePlusSolutionApplication : Application(), DIAware {

    override val di by DI.lazy {
        import(androidXModule(this@AcePlusSolutionApplication))

        //vmFactory
        bindSingleton { ViewModelFactory(di.direct) }

        //apiservice
        bindSingleton { RetrofitObj.API_SERVICE }

        //database
        bindSingleton { MovieDB.getInstance(instance()) }

        //repositories
        bindSingleton { MovieRepository(instance(), instance(), instance()) }


        //viewmodels
        bind<MovieViewModel>(MovieViewModel::class.java.simpleName) with provider {
            MovieViewModel(
                instance(),
                instance()
            )
        }

    }
}