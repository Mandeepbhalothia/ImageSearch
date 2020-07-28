package com.mandeep.imagesearch.repository

import com.mandeep.imagesearch.api.FlickerInstance
import com.mandeep.imagesearch.db.PhotoDao
import com.mandeep.imagesearch.model.Photo

class PhotoRepository(private val dao: PhotoDao) {

    suspend fun insertPhoto(photo: Photo) = dao.upsert(photo)

    suspend fun getAllPhotos(name: String) = dao.getAllPhotos(name)

    suspend fun getPhotosFromFlickr(name: String) = FlickerInstance.getPhotosFromFlicker(name)

}