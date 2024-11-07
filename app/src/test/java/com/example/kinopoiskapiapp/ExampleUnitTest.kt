package com.example.kinopoiskapiapp

import junit.framework.TestCase.assertEquals
import org.junit.Test

/*import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kinopoiskapiapp.core.database.AppDatabase
import com.example.kinopoiskapiapp.core.database.dao.MovieDao
import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith*/

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

/*@RunWith(AndroidJUnit4::class)
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
        val insertObserver = yourDao.insertMovie(MovieByIdDbo.empty()).subscribe()

        val testObserver = yourDao.getMovieById(0).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue { entity ->
            entity.id == 0
        }
    }
}*/
