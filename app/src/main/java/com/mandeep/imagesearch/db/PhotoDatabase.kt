package com.mandeep.imagesearch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mandeep.imagesearch.model.Photo

@Database(entities = [Photo::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun getPhotoDao(): PhotoDao

    companion object {

        @Volatile
        private var instance: PhotoDatabase? = null

        fun getDatabase(context: Context): PhotoDatabase {
            var tempInstance = instance
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                tempInstance = createDatabase(context)
                instance = tempInstance
                return tempInstance!!
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PhotoDatabase::class.java,
                "photoDb"
            ).build()


    }

}