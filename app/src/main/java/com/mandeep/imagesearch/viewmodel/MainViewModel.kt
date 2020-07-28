package com.mandeep.imagesearch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mandeep.imagesearch.db.PhotoDatabase
import com.mandeep.imagesearch.model.Photo
import com.mandeep.imagesearch.repository.PhotoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val photos: MutableLiveData<List<Photo>> = MutableLiveData()
    private val photoRepository: PhotoRepository
    private var columnsSelected: Int = 2
    private var lastQueriedText: String = ""

    init {
        val photoDao = PhotoDatabase.getDatabase(application).getPhotoDao()
        photoRepository = PhotoRepository(photoDao)
    }

    private fun insertPhoto(photo: Photo) = viewModelScope.launch {
        photoRepository.insertPhoto(photo)
    }

    fun getPhotosFromLocal(name: String) = viewModelScope.launch {
        val photoList = photoRepository.getAllPhotos(name)
        // first get old value then add new value, this will be useful in infinite scroll
        val previousValue = mutableListOf<Photo>()
        photos.value?.let {
            previousValue.addAll(it)
        }
        previousValue.addAll(photoList)

        photos.postValue(previousValue)
    }

    //IO scope is used for getting the data from other than Main thread
    fun getPhotosFromFlickr(name: String) = CoroutineScope(IO).launch {
        val photoList = photoRepository.getPhotosFromFlickr(name)
        // first get old value then add new value, this will be useful in infinite scroll
        val previousValue = mutableListOf<Photo>()
        photos.value?.let {
            previousValue.addAll(it)
        }
        previousValue.addAll(photoList)

        photos.postValue(previousValue)
        for (photo in photoList) {
            insertPhoto(photo)
        }
    }

    fun setNumberOfColumns(numberOfColumns: Int) {
        columnsSelected = numberOfColumns
    }

    fun getSelectedNoOfColumns(): Int = columnsSelected

    fun setLastQueriedText(queryText: String) {
        lastQueriedText = queryText
    }

    fun getLastQueriedText() = lastQueriedText

}