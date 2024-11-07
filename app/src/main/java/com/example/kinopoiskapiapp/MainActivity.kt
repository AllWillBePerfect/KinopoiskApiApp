package com.example.kinopoiskapiapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kinopoiskapiapp.core.database.AppDatabase
import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import com.example.kinopoiskapiapp.databinding.ActivityMainBinding
import com.example.kinopoiskapiapp.features.overview.deprecated.v1.OverviewFragment
import com.example.kinopoiskapiapp.features.overview.deprecated.v2.OverviewFragmentV2
import com.example.kinopoiskapiapp.features.overview.v3.OverviewFragmentV3
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_container, OverviewFragmentV3())
            commit()
        }


//        val apiService = ApiFactory.getKonopoiskApi()
//        val movies = apiService.getMoviesByName(1, 10, "мстители")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onSuccess = { movies -> printLog(movies.toString()) },
//                onError = { error -> printLog(error.message.toString()) }
//            )

        val dao = AppDatabase.getDatabase(this).movieDao()
        val insert = dao.nukeTable()
            .andThen(dao.insertMovie(MovieByIdDbo.empty(1)))
            .subscribeOn(Schedulers.io()).subscribeBy(
                onSuccess = { Log.d("MainActivity success insert", it.toString()) },
                onError = { Log.d("MainActivity failed insert", it.message.toString()) }
            )
    }

    fun printLog(message: String) = Log.d("MainActivity", message)


}