package com.example.kinopoiskapiapp.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kinopoiskapiapp.core.database.converters.KinopoiskTypeConverters
import com.example.kinopoiskapiapp.core.database.dao.MovieDao
import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [MovieByIdDbo::class], version = 1, exportSchema = false)
@TypeConverters(value = [KinopoiskTypeConverters::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        val passphrase: ByteArray = SQLiteDatabase.getBytes("super_secret_key".toCharArray())
        val factory = SupportFactory(passphrase)

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
//                    .openHelperFactory(factory)
                    .build()
                INSTANCE = instance
                instance
            }

        private const val DATABASE_NAME = "KinopoiskApp_db"
    }
}