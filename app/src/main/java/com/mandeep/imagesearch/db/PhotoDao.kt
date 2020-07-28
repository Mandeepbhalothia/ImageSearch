package com.mandeep.imagesearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mandeep.imagesearch.model.Photo

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(photo: Photo)

    @Query("select * from photos where queryName = :name")
    suspend fun getAllPhotos(name: String): List<Photo>

}