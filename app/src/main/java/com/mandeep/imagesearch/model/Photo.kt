package com.mandeep.imagesearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = false)
    var photoUrl: String,
    @ColumnInfo(name = "queryName")
    var queryName: String
)