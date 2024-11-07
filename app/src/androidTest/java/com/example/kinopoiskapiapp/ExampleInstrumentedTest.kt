package com.example.kinopoiskapiapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kinopoiskapiapp.core.database.AppDatabase
import com.example.kinopoiskapiapp.core.database.dao.MovieDao
import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import io.reactivex.Observable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.kinopoiskapiapp", appContext.packageName)
//    }
//}

//@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)
//class DatabaseUnitTest {
//
//    private lateinit var database: AppDatabase
//    private lateinit var dao: MovieDao
//
//    @Before
//    fun setup() {
//        // Создание in-memory базы данных
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            AppDatabase::class.java
//        ).allowMainThreadQueries().build()
//
//        dao = database.movieDao()
//    }
//
//    @After
//    fun tearDown() {
//        database.close()
//    }
//
//    @Test
//    fun insert_movie() {
//        val movie = MovieByIdDbo.empty()
//        dao.insertMovie(movie).subscribe()
//        dao.insertMovie(movie).subscribe()
//
//        dao.getMovieById(1)
//            .test()
//            .assertValue { it.id == 1}
//            .assertComplete()
//    }
//}

@RunWith(AndroidJUnit4::class)
class RoomDatabaseUnitTest {

    private lateinit var database: AppDatabase
    private lateinit var yourDao: MovieDao
    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        yourDao = database.movieDao()
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        database.close()
    }

    @Test
    fun insertAndRetrieveEntity() {

        val emptyMovie = MovieByIdDbo.empty(0)

        yourDao.insertMovie(emptyMovie)
            .flatMap { insertedId ->
                assert(insertedId == 0L) // Проверяем, что возвращенный ID верен
                yourDao.getMovieById(0) // Получаем объект по ID
            }
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { retrievedMovie ->
                retrievedMovie.name == null
            }

    }

    @Test
    fun insertAndRetrieveMultipleMovies() {
        // Создаем список объектов для вставки
        val movies = listOf(
            MovieByIdDbo.empty(0),
            MovieByIdDbo.empty(1),
            MovieByIdDbo.empty(2),
        )

        // Вставляем каждый объект в базу данных и подписываемся на Completable
        Observable.fromIterable(movies)
            .flatMapSingle { movie -> yourDao.insertMovie(movie) }
            .toList()
            .flatMap {
                // Получаем все записи из таблицы
                yourDao.getAllMovies() // Предположим, что такой метод существует
            }
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { retrievedMovies ->
                // Проверяем, что количество совпадает
                retrievedMovies.size == movies.size &&
                        // Проверяем, что каждый элемент по позиции совпадает по значению полей
                        retrievedMovies.zip(movies).all { (retrieved, original) ->
                            retrieved.id == original.id
//                                    &&
//                                    retrieved.title == original.title &&
//                                    retrieved.description == original.description
                        }
            }
    }


}


